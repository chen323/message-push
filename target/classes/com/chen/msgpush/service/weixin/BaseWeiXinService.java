package com.chen.msgpush.service.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.msgpush.constant.CacheConstant;
import com.chen.msgpush.constant.weixin.WxResponseStatusCode;
import com.chen.msgpush.exception.ApiBussException;
import com.chen.msgpush.model.domain.weixin.*;
import com.chen.msgpush.model.request.weixin.WxSendMsgReq;
import com.chen.msgpush.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 微信相关服务
 *
 * @author chen
 */
@Slf4j
public abstract class BaseWeiXinService {

    @Resource
    private HttpUtil httpUtil;

    /**
     * 获取对应的appId和secret
     */
    public abstract SecurityKeyInfo getAppIdAndSecret();
    
    /**
     * 用户授权token操作
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    private static final String CHECK_TOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";
    
    /**
     * 普通token操作,access_token的存储至少要保留512个字符空间。
     * access_token的有效期目前为2个小时，重复获取将导致上次获取的access_token失效。
     */
    private static final String NORMAL_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    
    private static final String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    
    /**
     * 强制获取代理对象，必须开启exposeProxy配置，否则获取不到当前代理对象
     *
     * @return 微信基础service对象
     */
    private BaseWeiXinService getWeixinService() {
        return (BaseWeiXinService) AopContext.currentProxy();
    }

    /**
     * 根据appid, secret, code获取accesstoken
     * <p>
     * 获取code后，请求以下链接获取access_token： https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * <p>
     * 正确时返回的JSON数据包如下：
     * <p>
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE"
     * }
     *
     * @param code 微信的授权码
     * @return 微信的token
     */
    public WxAccessToken getAccessTokenByCode(String code) {
        SecurityKeyInfo keyInfo = this.getAppIdAndSecret();
        String requestUrl = String.format(ACCESS_TOKEN_URL, keyInfo.getAppId(), keyInfo.getAppSecret(), code);
        return JSON.toJavaObject(getWeixinResponse(requestUrl), WxAccessToken.class);
    }



    @Cacheable(cacheNames = CacheConstant.MINUTE_FIVE_DURATION, key = "'wxNormalAccessToken' + #appid")
    public WxNormalAccessToken getNormalAccessToken(String appid, String secret) {
        String requestUrl = String.format(NORMAL_ACCESS_TOKEN_URL, appid, secret);
        return JSON.toJavaObject(getWeixinResponse(requestUrl), WxNormalAccessToken.class);
    }

    private String getNormalAccessToken() {
        SecurityKeyInfo keyInfo = this.getAppIdAndSecret();
        return getNormalAccessToken(keyInfo.getAppId(), keyInfo.getAppSecret()).getAccessToken();
    }


    public WxSendMsg postMsgSend(WxSendMsgReq req) {
        String token = this.getNormalAccessToken();
        String requestUrl = String.format(SEND_MSG_URL, token);
        return JSON.toJavaObject(postWeChatResponse(requestUrl, JSON.toJSONString(req)), WxSendMsg.class);
    }


    /**
     * 由于access_token拥有较短的有效期，当access_token超时后，
     * 可以使用refresh_token进行刷新，refresh_token有效期为30天，
     * 当refresh_token失效之后，需要用户重新授权。
     * <p>
     * 正确时返回的JSON数据包如下：
     * <p>
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE"
     * }
     *
     * @param refreshToken 刷新token
     * @return 微信的token
     */
    public WxAccessToken refreshToken(String refreshToken) {
        SecurityKeyInfo keyInfo = this.getAppIdAndSecret();
        String requestUrl = String.format(REFRESH_TOKEN_URL, keyInfo.getAppId(), refreshToken);
        return JSON.toJavaObject(getWeixinResponse(requestUrl), WxAccessToken.class);
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * 返回说明 正确的JSON返回结果：
     * <p>
     * { "errcode":0,"errmsg":"ok"}
     * 错误时的JSON返回示例：
     * <p>
     * { "errcode":40003,"errmsg":"invalid openid"}
     *
     * @param accessToken 微信的检验授权凭证
     * @param openId      微信的openid
     * @return 微信的token
     */
    public WxAccessToken checkAccessToken(String accessToken, String openId) {
        String requestUrl = String.format(CHECK_TOKEN_URL, accessToken, openId);
        return JSON.toJavaObject(getWeixinResponse(requestUrl), WxAccessToken.class);
    }


    /**
     * 通过URL获取微信请求
     *
     * @param requestUrl 请求url
     * @return 响应信息
     */
    private JSONObject getWeixinResponse(String requestUrl) {
        String rawResponse = httpUtil.doGet(requestUrl);
        return this.convertResponseWithCatchErrorCode(rawResponse);
    }

    /**
     * 通过URL获取微信请求
     *
     * @param requestUrl 请求url
     * @return 响应信息
     */
    private JSONObject postWeChatResponse(String requestUrl, String param) {
        String rawResponse = httpUtil.doPost(requestUrl, param);
        return this.convertResponseWithCatchErrorCode(rawResponse);
    }

    private JSONObject convertResponseWithCatchErrorCode(String rawResponse) {
        JSONObject response = JSON.parseObject(rawResponse);
        Integer errorCode = response.getInteger(WxBaseModel.ERR_CODE);
        String errorMsg = response.getString(WxBaseModel.ERR_MSG);
        log.info("BaseWeixinService response code: {}", errorCode);
        if (WxResponseStatusCode.REQUIRE_SUBSCRIBE.getCode().equals(errorCode) ||
                WxResponseStatusCode.CODE_BEEN_USED.getCode().equals(errorCode)) {
            log.warn("BaseWeixinService convertResponseWithCatchErrorCode has warn! rawResponse={}", rawResponse);
        } else if (Objects.nonNull(errorCode) && !WxResponseStatusCode.OK.getCode().equals(errorCode)) {
            throw new ApiBussException(errorCode, errorMsg);
        }
        return response;
    }

}