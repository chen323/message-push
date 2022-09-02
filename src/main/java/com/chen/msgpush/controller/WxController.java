package com.chen.msgpush.controller;

import com.chen.msgpush.constant.weixin.WxEventType;
import com.chen.msgpush.utils.WXUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.chen.msgpush.constant.PrefixConstant.URL_PREFIX;

/**
 * @author chen
 * @date 2022/9/2 2:11 下午
 */
@Slf4j
@RestController
@RequestMapping(URL_PREFIX)
@Api(tags = "微信相关接口")
@Validated
public class WxController {

    @RequestMapping(value = "/event", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("接受微信事件推送接口")
    public void acceptEvent(HttpServletRequest request){
        Map<String, String> eventParamMap = WXUtil.getEventParamMap(request);
        if (MapUtils.isEmpty(eventParamMap)){
            return;
        }
        String event = eventParamMap.get("Event");
        if (event.equals(WxEventType.SUBSCRIBE.getType())){
            //用户订阅，存入信息;
            //TODO
        }
    }
}
