package com.chen.msgpush.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class PropsServiceTest {

    @Resource
    private PropsService propsService;

    @Test
    public void setDataTest(){
        propsService.setData();
    }

}
