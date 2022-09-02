package com.chen.msgpush.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.msgpush.mapper.AppKeysMapper;
import com.chen.msgpush.model.domain.AppKeys;
import com.chen.msgpush.model.domain.weixin.SecurityKeyInfo;
import com.chen.msgpush.utils.aes.AESEncrypt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * <p>
 * 微信APP密钥 服务实现类
 * </p>
 *
 * @author chen
 */
@Service
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@Order(HIGHEST_PRECEDENCE)
public class AppKeysService extends ServiceImpl<AppKeysMapper, AppKeys> implements CommandLineRunner {

    private final Map<String, SecurityKeyInfo> securityMap = new HashMap<>();

    @Value("${wx.specialAssistant.appId}")
    private String appId;

    @Value("${encrypt.token}")
    private String vToken;


    private static final String EVENT_AES_KEY = "event.aes.key";
    private static final String EVENT_TOKEN = "event.token";
    private static final String WX_PAY_KEY = "wx.pay.key";

    public SecurityKeyInfo getSpecialAssistant() {
        return securityMap.get(appId);
    }


    public String getEventToken() {
        return securityMap.get(EVENT_TOKEN).getAppSecret();
    }

    public String getEventAesKey() {
        return securityMap.get(EVENT_AES_KEY).getAppSecret();
    }

    public String getWxPayKey() {
        return securityMap.get(WX_PAY_KEY).getAppSecret();
    }

    @Override
    public void run(String... args) throws Exception {
        List<AppKeys> all = this.list();
        for (AppKeys appKey : all) {
            String appId = appKey.getAppId();
            String encryptSecret = appKey.getAppSecret();
            // 解密密钥
            String decryptSecret = AESEncrypt.aesDecrypt(encryptSecret, vToken);
            SecurityKeyInfo securityKeyInfo = new SecurityKeyInfo(appId, decryptSecret);
            // 从数据库获取appid和security并存放到Map中
            securityMap.put(appId, securityKeyInfo);
        }
    }
}
