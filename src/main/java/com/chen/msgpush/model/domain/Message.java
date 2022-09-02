package com.chen.msgpush.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.msgpush.model.domain.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author nick.chen
 * @date 2022/9/2 4:29 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("message")
public class Message extends BaseModel<Message> {

    @TableField("template_id")
    private String templateId;

    @TableField("content")
    private String content;
}
