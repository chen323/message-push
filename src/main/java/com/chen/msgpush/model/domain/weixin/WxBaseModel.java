package com.chen.msgpush.model.domain.weixin;

import lombok.Data;

/**
 * @author chen
 */
@Data
public class WxBaseModel {

    public static final String ERR_CODE = "errcode";
    public static final String ERR_MSG = "errmsg";

    // 返回码
    private Integer errcode;
    // 对返回码的文本描述内容
    private String errmsg;
}
