package com.chen.msgpush.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.msgpush.model.domain.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置属性
 * </p>
 *
 * @author rachel.ran
 * @since 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("props")
public class Props extends BaseModel<Props> {

    private static final long serialVersionUID = 1L;

    /**
     * 属性键
     */
    @TableField("prop_key")
    private String propKey;

    /**
     * 属性值
     */
    @TableField("prop_value")
    private String propValue;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

}
