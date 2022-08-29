package com.chen.msgpush.model.domain.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wayne
 * @version WxNormalAccessToken,  2020/11/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WxNormalAccessToken extends WxBaseModel {
    private String accessToken;
    private Integer expiresIn;
}
