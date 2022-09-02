package com.chen.msgpush.constant;

import lombok.Getter;

/**
 * @author nick.chen
 * @date 2022/9/2 5:47 下午
 */
@Getter
public enum HeFengResponseCode {
    /**
     * 和风天气相应状态码
     */
    UNKNOWN("000", "未知错误"),
    SUCCESS("200", "请求成功"),
    NO_DATA("204", "查询的地区暂时没有数据"),
    PARAM_ERROR("400", "请求参数错误"),
    AUTH_FAIL("401", "认证失败"),
    ACCESS_NUM_NOT_ENOUGH("402", "超过访问次数或余额不足以支持继续访问服务"),
    NO_AUTH("403", "无访问权限"),
    NOT_FUND("404", "查询的数据或地区不存在"),
    OVER_QPM("429", "超过限定的QPM"),
    SERVER_ERROR("500", "服务器粗错误"),
    ;

    HeFengResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HeFengResponseCode getByCode(String code){
        HeFengResponseCode[] values = HeFengResponseCode.values();
        for (HeFengResponseCode value : values) {
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return UNKNOWN;
    }

    private final String code;

    private final String desc;
}
