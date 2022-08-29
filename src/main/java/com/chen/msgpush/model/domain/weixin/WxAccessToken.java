package com.chen.msgpush.model.domain.weixin;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WxAccessToken extends WxBaseModel {
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;
}
