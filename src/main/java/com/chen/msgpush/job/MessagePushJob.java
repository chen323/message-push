package com.chen.msgpush.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.msgpush.model.domain.Message;
import com.chen.msgpush.model.domain.Props;
import com.chen.msgpush.model.domain.UserInfo;
import com.chen.msgpush.model.request.weixin.WxSendMsgReq;
import com.chen.msgpush.service.MessageService;
import com.chen.msgpush.service.PropsService;
import com.chen.msgpush.service.UserInfoService;
import com.chen.msgpush.service.weixin.SpecialAssistantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author nick.chen
 * @date 2022/9/2 4:21 下午
 */

@Component
@Slf4j
public class MessagePushJob {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private MessageService messageService;

    @Resource
    private SpecialAssistantService specialAssistantService;

    @Resource
    private PropsService propsService;

    @Value("${template_id}")
    private String templateId;

    @Value("${message.push.job.enable}")
    private Boolean enable;

    @Scheduled(cron = "${message.push.job}")
    public void execute(){
        if (!enable){
            return;
        }
        List<UserInfo> list = userInfoService.list();
        Message message = messageService.getByTemplateId(templateId);
        Props props = propsService.getByKey(templateId);

        WxSendMsgReq wxSendMsgReq = new WxSendMsgReq();
        wxSendMsgReq.setTemplate_id(templateId);
        for (UserInfo userInfo : list) {
            wxSendMsgReq.setTouser(userInfo.getOpenId());
            specialAssistantService.postMsgSend(wxSendMsgReq);
        }
    }

}
