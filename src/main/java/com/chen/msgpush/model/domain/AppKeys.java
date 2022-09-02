package com.chen.msgpush.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.msgpush.model.domain.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信APP密钥
 * </p>
 *
 * @author chen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_keys")
public class AppKeys extends BaseModel<AppKeys> {

    private static final long serialVersionUID = 1L;

    /**
     * 微信APP ID
     */
    @TableField("app_id")
    private String appId;

    /**
     * 微信APP 密钥 存储加密之后的值
     */
    @TableField("app_secret")
    private String appSecret;

    /**
     * 微信APP描述
     */
    @TableField("description")
    private String description;

    /**
     * 企业微信:应用类型
     */
    @TableField("type")
    private String type;

}
