package com.chen.msgpush.advice;

import com.chen.msgpush.model.response.ServiceResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一json返回处理
 */
@ControllerAdvice(basePackages = "com.chen.msgpush.controller")
public class ResultJsonResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ServiceResponse) {
            return ((ServiceResponse) body).toMap();
        } else {
            return ServiceResponse.success(body);
        }
    }
}
