package com.chen.msgpush.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.msgpush.constant.HeFengResponseCode;
import com.chen.msgpush.exception.ApiBussException;
import com.chen.msgpush.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author nick.chen
 * @date 2022/9/2 5:39 下午
 */
@Service
public class HeFengWeather {

    @Resource
    private HttpUtil httpUtil;

    @Value("${hefeng.weather.key}")
    private String key;

    private String getCityWeatherUrl = "https://devapi.qweather.com/v7/weather/now?%s";

    private String getCityLocationUrl = "https://geoapi.qweather.com/v2/city/lookup?location=%s&key=%s";

    public String getCityLocationId(String cityName){
        String url = String.format(getCityLocationUrl, cityName, key);
        String res = httpUtil.doGet(url);
        JSONObject jsonObject = JSON.parseObject(res);
        String code = HeFengResponseCode.SUCCESS.getCode();
        if (HeFengResponseCode.SUCCESS.getCode().equals(jsonObject.get("code"))){
           return String.valueOf(jsonObject.getJSONArray("location").getJSONObject(0).get("id"));
        }else {
            HeFengResponseCode responseCode = HeFengResponseCode.getByCode(code);
            throw new ApiBussException(Integer.parseInt(responseCode.getCode()), responseCode.getDesc());
        }
    }
}
