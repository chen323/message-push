package com.chen.msgpush.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.msgpush.model.domain.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author chen
 * @date 2022/9/2 4:10 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_info")
public class UserInfo extends BaseModel<UserInfo> {

    @TableField("open_id")
    private String openId;

    @TableField("union_id")
    private String unionId;

    @TableField("phone")
    private String phone;
}
