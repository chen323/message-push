package com.chen.msgpush.advice;

import com.chen.msgpush.constant.ResponseStatusCode;
import com.chen.msgpush.constant.SystemConstant;
import com.chen.msgpush.exception.ApiBussException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 */
@Slf4j
@ControllerAdvice(basePackages = "com.chen.msgpush.controller")
public class RequestBodyValidateAdvice extends RequestBodyAdviceAdapter {

    @Resource
    private LocalValidatorFactoryBean localValidatorFactoryBean;


    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {


        // 验证参数
        List<ConstraintViolation<Object>> validErrors =
                new ArrayList<>(this.localValidatorFactoryBean.validate(body, Default.class));

        if (!validErrors.isEmpty()) {
            throw new ApiBussException(ResponseStatusCode.INPUT_PARAM_ERROR.getCode(),
                    SystemConstant.getFormatMsg(ResponseStatusCode.INPUT_PARAM_ERROR.getDescription(), validErrors.get(0).getMessageTemplate()));
        }

        return body;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
}
