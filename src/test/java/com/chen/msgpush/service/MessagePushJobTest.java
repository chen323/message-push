package com.chen.msgpush.service;

import com.chen.msgpush.job.MessagePushJob;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MessagePushJobTest {

    @Resource
    private MessagePushJob messagePushJob;

    @Test
    public void test001(){
        messagePushJob.execute();
    }
}
