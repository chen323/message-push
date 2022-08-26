package com.chen.msgpush.advice;

import com.chen.msgpush.constant.ResponseStatusCode;
import com.chen.msgpush.constant.SystemConstant;
import com.chen.msgpush.exception.ApiBussException;
import com.chen.msgpush.model.response.ServiceResponse;
import com.chen.msgpush.utils.FileUtil;
import com.chen.msgpush.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * 统一处理controller类抛出的ApiBussException异常，转换成标准json输出格式信息
 * Created by jackie.yu on 2017/7/27.
 * 参考 springboot Error Handling
 * 
 * @author chen
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Resource
    private MultipartConfigElement multipartConfigElement;

    @Resource
    private RedisUtil redisUtil;

    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Map<String, Object>> handleControllerException(HttpServletRequest request, Throwable ex) {

        // 默认500，服务器内部异常
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(ServiceResponse.failure(ResponseStatusCode.ABNORMAL.getCode(),
                        ResponseStatusCode.ABNORMAL.getDescription()), HttpStatus.INTERNAL_SERVER_ERROR);
        // 自定义异常
        if (ex instanceof ApiBussException) {
            int statusCode = ((ApiBussException) ex).getErrorCode();
            String errorMsg = ((ApiBussException) ex).getErrorMsg();
            responseEntity = ResponseEntity.ok().body(ServiceResponse.failure(statusCode, errorMsg));
        }
        // 文件过大异常
        if (ex instanceof MaxUploadSizeExceededException) {
            long maxFileSize = multipartConfigElement.getMaxFileSize();
            String formatFileSize = FileUtil.formatFileSize(maxFileSize);
            responseEntity = ResponseEntity.ok().body(ServiceResponse.failure(ResponseStatusCode.ABNORMAL.getCode(),
                    SystemConstant.getFormatMsg(ResponseStatusCode.ABNORMAL.getDescription(), formatFileSize)));
        }

        //参数校验
        String message = StringUtils.EMPTY;
        if (ex instanceof ConstraintViolationException) {
            message = this.getConstraintViolationExceptionMsg(ex);
        }

        if (ex instanceof MissingServletRequestParameterException) {
            message = this.getMissingServletRequestParameterExceptionMsg(ex);
        }

        if (StringUtils.isNotBlank(message)) {
            responseEntity = new ResponseEntity<>(
                    ServiceResponse.failure(ResponseStatusCode.INPUT_PARAM_ERROR.getCode(),
                            SystemConstant.getFormatMsg(ResponseStatusCode.INPUT_PARAM_ERROR.getDescription(), message)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.error("接口访问异常[" + request.getRequestURI() + "]", ex);
        //除了自定义异常，其余异常返回未知错误信息
        return responseEntity;
    }

    /**
     * 处理request parameters参数错误
     *
     * @param ex 捕捉到的异常
     * @return 错误信息
     */
    private String getConstraintViolationExceptionMsg(Throwable ex) {
        String message = StringUtils.EMPTY;
        String originErrorMsg = ex.getMessage();
        if (StringUtils.isNotBlank(originErrorMsg)) {
            String[] errMsgs = originErrorMsg.split(",");
            // 取第一个错误
            message = StringUtils.trim(errMsgs[0].split(":")[1]);
        }
        return message;
    }

    /**
     * 处理参数缺失错误
     *
     * @param ex 捕捉到的异常
     * @return 错误信息
     */
    private String getMissingServletRequestParameterExceptionMsg(Throwable ex) {
        String message = StringUtils.EMPTY;
        String originErrorMsg = ex.getMessage();
        if (StringUtils.isNotBlank(originErrorMsg)) {
            String[] errMsgs = originErrorMsg.split(",");
            if (errMsgs.length > 1) {
                // 取第一个错误
                message = errMsgs[0].split(":")[1];
            } else {
                message = StringUtils.trim(originErrorMsg.split(":")[0]);
            }
        }
        return message;
    }

}
