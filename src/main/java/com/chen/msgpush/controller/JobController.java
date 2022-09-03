package com.chen.msgpush.controller;


import com.chen.msgpush.job.MessagePushJob;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.chen.msgpush.constant.PrefixConstant.URL_PREFIX;


/**
 * @author CJQ
 */
@Slf4j
@RestController
@RequestMapping(URL_PREFIX)
@Api(tags = "微信相关接口")
@Validated
public class JobController {

    @Resource
    private MessagePushJob messagePushJob;

    @GetMapping("/messagePush")
    public boolean messagePush(){
        messagePushJob.execute();
        return true;
    }
}
