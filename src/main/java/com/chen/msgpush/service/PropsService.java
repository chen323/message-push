package com.chen.msgpush.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.msgpush.mapper.PropsMapper;
import com.chen.msgpush.model.domain.Props;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
