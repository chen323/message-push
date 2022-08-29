package com.chen.msgpush.model.request.weixin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by arden.chen on 2020/12/25/025.
 */
@Data
public class WxSendMsgReq {

    @ApiModelProperty("接收者openid")
    private String touser;

    @ApiModelProperty("模板ID")
    private String template_id;

    @ApiModelProperty("模板跳转链接（海外帐号没有跳转能力）")
    private String url;

    @ApiModelProperty("跳小程序所需数据，不需跳小程序可不用传该数据")
    private Map<String, String> miniprogram;

    @ApiModelProperty("模板数据")
    private Map<String, Map<String, String>> data;

}
