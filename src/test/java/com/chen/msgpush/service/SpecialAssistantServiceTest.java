package com.chen.msgpush.service;

import com.chen.msgpush.model.domain.weixin.SecurityKeyInfo;
import com.chen.msgpush.model.domain.weixin.WxNormalAccessToken;
import com.chen.msgpush.model.domain.weixin.WxSendMsg;
import com.chen.msgpush.model.request.weixin.WxSendMsgReq;
import com.chen.msgpush.service.weixin.SpecialAssistantService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen
 *  2022/8/29 4:57 下午
 */
@SpringBootTest
@Slf4j
public class SpecialAssistantServiceTest {

    @Resource
    private SpecialAssistantService specialAssistantService;

    @Test
    public void test001(){
        SecurityKeyInfo appIdAndSecret = specialAssistantService.getAppIdAndSecret();
        System.out.println(appIdAndSecret);
    }

    @Test
    public void sendMsg(){
        SecurityKeyInfo appIdAndSecret = specialAssistantService.getAppIdAndSecret();
        WxSendMsgReq wxSendMsgReq = new WxSendMsgReq();
        wxSendMsgReq.setTemplate_id("Zct_Y2LxEOI2z8bD7V5FQzvso_BAZQuUoEJBriU5UPg");
        wxSendMsgReq.setTouser("oHA2O5wbtHakbCe3JcXxDf6FRng8");
        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("date", buildDetail("2022-08-29", "#173177"));
        data.put("region", buildDetail("中国", "#173177"));
        data.put("weather", buildDetail("阴", "#173177"));
        data.put("temp", buildDetail("30摄氏度", "#173177"));
        data.put("wind_dir", buildDetail("3级", "#173177"));
        data.put("love_day", buildDetail("50", "#173177"));
        data.put("birthday1", buildDetail("2020-01-01", "#173177"));
        data.put("note", buildDetail("这是一条消息", "#173177"));
        wxSendMsgReq.setData(data);
        WxSendMsg wxSendMsg = specialAssistantService.postMsgSend(wxSendMsgReq);
        log.info("res ==> {}", wxSendMsg);
    }

    private Map<String, String> buildDetail(String value, String color) {
        Map<String, String> detail = Maps.newHashMap();
        detail.put("value", value);
        detail.put("color", color);
        return detail;
    }
}
