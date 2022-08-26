package com.chen.msgpush.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chen
 */
@Component
public class ModelFillHandlerConfig implements MetaObjectHandler {
    /**
     * 在插入的时候填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        setCreateTimeColumn(metaObject);
        setUpdateTimeColumn(metaObject);
    }

    /**
     * 在更新的时候填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //setUpdateTimeColumn(metaObject);
    }

    private void setCreateTimeColumn(MetaObject metaObject) {
        // 判断数据库中是否有create_time 列， 如果有就插入数据
        if (metaObject.hasSetter("createTime")) {
            setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    private void setUpdateTimeColumn(MetaObject metaObject) {
        // 判断数据库中是否有update_time 列， 如果有就插入数据
        if (metaObject.hasSetter("updateTime")) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
    }
}
