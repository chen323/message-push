package com.chen.msgpush.model.response;

import com.chen.msgpush.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel(value = "ServiceResponse", description = "返回封装类")
@Slf4j
public class ServiceResponse {
    @ApiModelProperty("返回状态，true-成功，false-失败")
    private Boolean ret;
    @ApiModelProperty("具体数据")
    private Object res;

    public static Map<String, Object> success(Object res) {
        return new ServiceResponse(true, res).toMap();
    }

    public static Map<String, Object> failure(Object res) {
        return new ServiceResponse(false, res).toMap();
    }

    public static Map<String, Object> failure(Integer errorCode, String errorMessage) {
        Map<String, Object> result = new HashMap<>();
        result.put("errCode", errorCode);
        result.put("errMsg", errorMessage);
        return new ServiceResponse(false, result).toMap();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("ret", this.getRet());
        result.put("res", this.getRes());
        return result;
    }

    private ServiceResponse(Boolean ret, Object res) {
        this(ret, res, DateUtil.getTenLengthTimeStampByDate(new Date()), "");
    }

    private ServiceResponse(Boolean ret, Object res, int nonce, String sid) {
        this.ret = ret;
        this.res = res;
    }
}


