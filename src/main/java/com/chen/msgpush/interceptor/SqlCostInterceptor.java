package com.chen.msgpush.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
                Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Component
@Slf4j
public class SqlCostInterceptor implements Interceptor, Ordered {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS"));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();

            String realRunnedSql = getRealRunningSQL(boundSql, configuration);

            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            log.info("============>SQL：[{}, 执行耗时: {} ms]==========", realRunnedSql, costTime);
        }
    }

    /**
     * 获取完整的sql实体的信息
     *
     * @param boundSql      sql 语句对象
     * @param configuration 配置对象
     * @return sql语句
     * @see org.apache.ibatis.scripting.defaults.DefaultParameterHandler 参考Mybatis 参数处理
     */
    private String getRealRunningSQL(BoundSql boundSql, Configuration configuration) {
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // 输入sql字符串空判断
        if (Strings.isBlank(sql) || Objects.isNull(configuration) || Objects.isNull(parameterMappings)) {
            return Strings.EMPTY;
        }

        // 美化sql，清除多余的空白行
        sql = sql.replaceAll("[\\s\n ]+", " ");
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        Object parameterObject = boundSql.getParameterObject();
        String fieldComment;
        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() != ParameterMode.OUT) {
                Object value;
                String propertyName = parameterMapping.getProperty();
                if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                } else if (parameterObject == null) {
                    value = null;
                } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                StringBuilder paramValueStr = new StringBuilder();
                if (value instanceof Date) {
                    paramValueStr.append("'").append(DATE_FORMAT_THREAD_LOCAL.get().format(value)).append("'");
                } else {
                    paramValueStr.append("'").append(value).append("'");
                }
                // mybatis generator 中的参数不打印出来
//                if (!propertyName.contains("frch_criterion")) {
//                    fieldComment = "/*" + propertyName + "*/";
//                    paramValueStr.insert(0, fieldComment);
//                }
                // 将UPDATE 语句中的 =? 替换为=参数值
                if (sql.startsWith("UPDATE")) {
                    sql = sql.replaceFirst("=\\?", "=" + Matcher.quoteReplacement(paramValueStr.toString()));
                } else {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(paramValueStr.toString()));

                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}