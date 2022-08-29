package com.chen.msgpush.model.domain.weixin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityKeyInfo {
    private String appId;
    private String appSecret;
}
