package com.chen.msgpush.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.msgpush.mapper.MessageMapper;
import com.chen.msgpush.model.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nick.chen
 * @date 2022/9/2 4:30 下午
 */
@Service
@Slf4j
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    public Message getByTemplateId(String templateId){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Message::getTemplateId, templateId);
        return getOne(queryWrapper);
    }

    public List<Message> listByTemplateId(String templateId){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Message::getTemplateId, templateId);
        return list(queryWrapper);
    }

}
