package com.chen.msgpush.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SpringBootTest
@Slf4j
public class HeFengWeatherServiceTest {

    @Resource
    private HeFengWeatherService heFengWeatherService;

    @Test
    public void testGetWeather() throws IOException {
        String locationId = heFengWeatherService.getCityLocationId("武汉");
        Map<String, String> res = heFengWeatherService.getCityNowWeather(locationId);
        log.info("res ===> {}", res);
    }
}
