package com.chen.msgpush.model.domain.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rachel.ran on 2019-11-30.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class BaseModel<T extends BaseModel<T>> extends Model<T> {

    public static final String CREATE_TIME_COLUMN = "create_time";
    public static final String ID_COLUMN = "id";

    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 是否删除(1：删除 0:不删除)
     */
    @TableLogic
    @TableField(value = "is_delete")
    public Boolean isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    public Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", update = "now()", fill = FieldFill.INSERT_UPDATE)
    public Date updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
