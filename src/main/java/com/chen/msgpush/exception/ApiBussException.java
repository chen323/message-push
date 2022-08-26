package com.chen.msgpush.exception;

import com.chen.msgpush.constant.ResponseStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * *
 * 所有service的异常通过该类抛出来，AOP层捕捉进行业务处理后返回
 *
 * @author chen
 * @version 1.0
 * @since 2015/10/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiBussException extends RuntimeException {

    private static final long serialVersionUID = -8415209419043592516L;

    /**
     * 错误码
     */
    private final Integer errorCode;

    /**
     * 错误描述
     */
    private final String errorMsg;

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param errorMsg  错误描述
     * @param cause     异常
     */
    public ApiBussException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorCode.toString().concat(errorMsg), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param errorMsg  错误描述
     */
    public ApiBussException(Integer errorCode, String errorMsg) {
        this(errorCode, errorMsg, null);
    }

    public ApiBussException(ResponseStatusCode responseStatusCode) {
        this(responseStatusCode.getCode(), responseStatusCode.getDescription(), null);
    }
    /**
     * 构造函数
     *
     * @param errorMsg  错误描述
     */
    public ApiBussException(String errorMsg) {
        this(null,errorMsg, null);
    }
}
