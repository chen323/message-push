package com.chen.msgpush.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.msgpush.mapper.PropsMapper;
import com.chen.msgpush.model.domain.Props;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统配置属性 服务实现类
 * </p>
 *
 * @author rachel.ran
 * @since 2021-09-18
 */
@Service
public class PropsService extends ServiceImpl<PropsMapper, Props> {

    public Props getByKey(String key) {
        QueryWrapper<Props> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Props::getPropKey, key);
        return this.getOne(wrapper);
    }


    public void addOrUpdate(Props props) {
        QueryWrapper<Props> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Props::getPropKey, props.getPropKey());
        this.saveOrUpdate(props, wrapper);
    }

    public List<Props> listByKey(String key) {
        QueryWrapper<Props> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Props::getPropKey, key);
        return this.list(wrapper);
    }

    public void setData(){
        Map<String, String> data = new HashMap<>(16);
        data.put("region", "武汉");
        data.put("loveDay", "2016-06-11");
        data.put("birthday","07-14");
        Props props = new Props();
        props.setPropKey("Zct_Y2LxEOI2z8bD7V5FQzvso_BAZQuUoEJBriU5UPg");
        props.setPropValue(JSON.toJSONString(data));
        saveOrUpdateByKey(props);
    }

    public void saveOrUpdateByKey(Props props){
        QueryWrapper<Props> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Props::getPropKey, props.getPropKey());
        saveOrUpdate(props,queryWrapper);
    }
}
