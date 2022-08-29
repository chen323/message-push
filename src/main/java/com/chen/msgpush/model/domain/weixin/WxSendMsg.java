package com.chen.msgpush.model.domain.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by arden.chen on 2020/12/25/025.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WxSendMsg extends WxBaseModel {
    private Long msgid;
}
