package com.chen.msgpush.service.weixin;

import com.chen.msgpush.model.domain.weixin.SecurityKeyInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * @author chen
 *  2022/8/29 1:47 下午
 */
@Service
public class SpecialAssistantService extends BaseWeiXinService implements CommandLineRunner {

    @Value("${wx.specialAssistant.appId}")
    private String appId;

    @Value("${wx.specialAssistant.secret}")
    private String secret;

    private SecurityKeyInfo securityKeyInfo;

    @Override
    public SecurityKeyInfo getAppIdAndSecret() {
        return securityKeyInfo;
    }

    @Override
    public void run(String... args) {
        securityKeyInfo = new SecurityKeyInfo(appId, secret);
    }
}
