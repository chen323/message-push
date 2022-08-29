package com.chen.msgpush.constant.weixin;

import lombok.Getter;
import lombok.ToString;

/**
 * 微信返回值状态码
 * @author chen
 */
@Getter
@ToString
public enum WxResponseStatusCode {

    SYSTEM_IS_BUSY(-1, "系统繁忙"),
    OK(0, "ok"),
    CODE_BEEN_USED(40163, "授权码重复使用"),
    REQUIRE_SUBSCRIBE(43004, "需要接收者关注"),
    ;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String description;

    /**
     * @param code        错误码
     * @param description 错误描述
     */
    WxResponseStatusCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据错误码返回企业微信状态码
     *
     * @param code 错误码
     * @return 企业微信返回值状态码
     */
    public static WxResponseStatusCode getByCode(Integer code) {
        WxResponseStatusCode[] list = WxResponseStatusCode.values();

        for (WxResponseStatusCode wwResponseStatusCode : list) {
            if (code.equals(wwResponseStatusCode.getCode())) {
                return wwResponseStatusCode;
            }
        }

        return null;
    }
}
