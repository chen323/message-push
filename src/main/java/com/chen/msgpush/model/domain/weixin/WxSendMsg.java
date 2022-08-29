package com.chen.msgpush.model.domain.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WxSendMsg extends WxBaseModel {
    private Long msgid;
}
