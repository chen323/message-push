package com.chen.msgpush.constant.weixin;

import lombok.Getter;

/**
 * @author chen
 * @date 2022/9/2 2:08 下午
 */
@Getter
public enum WxEventType {
    /**
     * 微信事件类型
     */
    SUBSCRIBE("subscribe", "用户订阅"),
    UNSUBSCRIBE("unsubscribe", "用户取消订阅"),
    ;

    WxEventType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private final String type;

    private final String desc;


}
