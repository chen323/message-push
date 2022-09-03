package com.chen.msgpush.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.msgpush.model.domain.Message;
import com.chen.msgpush.model.domain.Props;
import com.chen.msgpush.model.domain.UserInfo;
import com.chen.msgpush.model.request.weixin.WxSendMsgReq;
import com.chen.msgpush.service.HeFengWeatherService;
import com.chen.msgpush.service.MessageService;
import com.chen.msgpush.service.PropsService;
import com.chen.msgpush.service.UserInfoService;
import com.chen.msgpush.service.weixin.SpecialAssistantService;
import com.chen.msgpush.utils.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

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
    private HeFengWeatherService heFengWeatherService;

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

        WxSendMsgReq wxSendMsgReq = buildMessage1(templateId);
        for (UserInfo userInfo : list) {
            wxSendMsgReq.setTouser(userInfo.getOpenId());
            specialAssistantService.postMsgSend(wxSendMsgReq);
        }
    }

    private WxSendMsgReq buildMessage1(String templateId){
        WxSendMsgReq wxSendMsgReq = new WxSendMsgReq();
        wxSendMsgReq.setTemplate_id(templateId);
        Props props = propsService.getByKey(templateId);
        Map<String, String> propMap = JSON.parseObject(props.getPropValue(), new TypeReference<Map<String, String>>() {});
        String region = propMap.get("region");
        String birthdayMonthAndDay = propMap.get("birthday");
        String loveDay = propMap.get("loveDay");
        //获取天气信息
        Map<String, String> weatherMap = heFengWeatherService.getCityNowWeatherByName(region);
        String weather = weatherMap.get("text");
        String temp = weatherMap.get("temp") + "℃";
        String windDir = weatherMap.get("windDir") + weatherMap.get("windScale") + "级";
        Calendar calendar = Calendar.getInstance();
        String date = DateUtil.dateFormat(new Date(), DateUtil.fmt_yMdTime);
        String[] weekStr = {"星期日", "星期一","星期二", "星期三", "星期四", "星期五", "星期六"};
        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        date = date + " " + weekStr[i];
        Integer loveDays = DateUtil.daysBetweenDate(DateUtil.strToDate(loveDay), new Date());
        String birthday = calendar.get(Calendar.YEAR) + "-" +  birthdayMonthAndDay;
        if (new Date().after(DateUtil.strToDate(birthday))){
            birthday = calendar.get(Calendar.YEAR) + 1 + "-" +  birthdayMonthAndDay;
        }
        int birthdayDay = Math.abs(DateUtil.daysBetweenDate(DateUtil.strToDate(birthday), new Date()));
        //获取随机消息
        List<Message> messages = messageService.listByTemplateId(templateId);
        String note;
        if (!CollectionUtils.isEmpty(messages)) {
            Random random = new Random();
            int num = random.nextInt(messages.size());
            Message message = messages.get(num);
            note = message.getContent();
        }else {
            note = "哎呀，对你说的话好像空了呢，别着急，是没有配置啦，快喊你的猪去配置吧";
        }
        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("date", buildDetail(date, "#22acd0"));
        data.put("region", buildDetail(region, "#173177"));
        data.put("weather", buildDetail(weather, "#faae6b"));
        data.put("temp", buildDetail(temp, "#01aed0"));
        data.put("windDir", buildDetail(windDir, "#03b6a4"));
        data.put("loveDay", buildDetail(loveDays.toString(), "#e14f90"));
        data.put("birthdayDay", buildDetail(Integer.toString(birthdayDay), "#173177"));
        data.put("note", buildDetail(note, "#9c4ca0"));
        wxSendMsgReq.setData(data);
        return wxSendMsgReq;
    }


    private Map<String, String> buildDetail(String value, String color) {
        Map<String, String> detail = Maps.newHashMap();
        detail.put("value", value);
        detail.put("color", color);
        return detail;
    }

    public static void main(String[] args) {
        Date date = DateUtil.strToDate("2022-09-05");
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int i = instance.get(Calendar.DAY_OF_WEEK);
        System.out.println(i);
    }
}
