package com.chen.msgpush.constant;

import lombok.Getter;
import lombok.ToString;

/**
 * 返回值状态码，000000表示请求处理成功，999999表示系统内部错误。
 * 错误码统一为6位，第1位表示错误类型，第2、3位表示具体错误类型，第4、5、6位表示具体错误。
 * @author chen
 */
@Getter
@ToString
public enum ResponseStatusCode {
    ABNORMAL(999999, "系统内部错误"),
    INPUT_PARAM_ERROR(999993, "参数异常：{0}"),
    OPERATION_FAILED(999991, "操作失败"),

    // 101 微信类错误
    WEIXIN_ACCESS_TOKEN_FAILED(101001, "获取微信access_token失败"),
    WEIXIN_JSAPI_TICKEN_FAILED(101002, "获取微信jsapi_ticket失败"),
    WEIXIN_ENCRIPY_FAILED(101003,"微信加密失败"),

    WEIXIN_PAY_UNIFIED_ORDER_FAILED(101101,"微信支付创建订单失败"),
    WEIXIN_PAY_QUERY_ORDER_FAILED(101102,"微信支付查询订单失败"),
    WEIXIN_REFUND_QUERY_ORDER_FAILED(101103,"微信支付推荐查询失败"),
    WEIXIN_DOWNLOAD_BILL_FAILED(101104,"微信支付下载对账单失败"),
    WEIXIN_CLOSE_ORDER_FAILED(101105,"微信支付关闭订单失败"),
    WEIXIN_PAY_ACTIVITY_FAILED(101106,"活动套餐参数错误"),
    WEIXIN_CLOSE_ORDER_ERROR_OUT_TRADE_NO(101107,"微信支付关闭订单商户订单号无效"),
    WEIXIN_PAY_CLIENT_FILL_FAILED(101108,"微信支付客户端编码请求失败"),
    WEIXIN_PAY_NOTIFY_FAILED(101109,"微信支付支付通知失败"),
    WEIXIN_PAY_COURSE_FAILED(101110, "课程参数错误"),
    WEIXIN_APP_ID_NOT_EXIST(101111, "微信服务号不存在"),
    WEIXIN_GET_QRCODE_FAIL(10112,"生成二维码失败"),

    // 102 企业微信类错误
    WORK_WEIXIN_ACCESS_TOKEN_FAILED(102001, "获取企业微信access_token失败"),
    WORK_WEIXIN_POST_FAILED(102002, "企业微信POST失败"),
    WORK_WEIXIN_GET_FAILED(102003, "企业微信GET失败"),
    WORK_WEIXIN_UPLOAD_FAILED(102003, "企业微信UPLOAD失败"),
    // 103 活动类错误
    ACTIVITY_NOT_EXIST(103001, "活动不存在"),
    ACTIVITY_ALREADY_EXIST(103002, "活动已存在"),
    ACTIVITY_SECKILL_CREATE_FAILED(103003, "秒杀名额构建失败"),
    ACTIVITY_START_TIME_MUST_BEFORE_END_TIME(103003, "结束时间必须大于开始时间"),
    ACTIVITY_PACKAGE_TAIL_VALUE_ERROR(103004, "定金为0时，尾款必须为0"),
    ACTIVITY_IS_FINISHED(103005, "活动已结束，不能开启"),
    // 104 引流类错误
    TRAFFIC_NOT_EXIST(104001, "引流数据不存在"),
    TRAFFIC_ALREADY_EXIST(104002, "引流数据已存在"),
    TRAFFIC_AGENT_IS_EMPTY(104003, "引流客服不存在"),
    TRAFFIC_GROUP_IS_EMPTY(104004, "引流群聊不存在"),
    TRAFFIC_AGENT_TYPE_ERROR(104005, "引流客服属于不同主体"),
    //105 企业微信事件类错误
    WORK_WX_EVENT_VERIFY_FAILED(105001, "企业微信事件URL认证失败"),
    WORK_WX_EVENT_HANDLE_FAILED(105002, "企业微信事件处理失败"),

    //106用户类错误
    CUSTOMER_PHONE_EXIST(106001, "该手机号码已存在"),
    CUSTOMER_NOT_EXIST(106002, "客户不存在"),
    CUSTOMER_ALREADY_EXIST(106003, "客户已存在"),
    CUSTOMER_ID_NOT_EXIST(106004, "客户号不存在"),
    CUSTOMER_NOT_AUTH(106005, "客户未授权"),
    CUSTOMER_COURSE_CATALOG_NOT_EXIST(106006, "客户的课程小节不存在"),
    CUSTOMER_COURSE_CATALOG_LOCK(106007, "客户的课程小节还未解锁"),
    //107 群聊类错误
    GROUP_NOT_EXIST(107001, "群聊不存在"),

    //108 课程类错误
    COURSE_RESOURCE_NOT_EXIST(108001, "课程资源不存在"),
    COURSE_CATALOG_NOT_EXIST(108002, "课程小节不存在"),
    COURSE_UNLOCK_STATUS(108003,"课程不能完全解锁"),
    COURSE_NOT_EXIST(108004, "课程不存在"),
    COURSE_CAN_NOT_DELETE(108005,"此课程已有客户购买，不能删除"),
    COURSE_AGENT_IS_EMPTY(108006, "课程二维码不存在"),
    COURSE_BOUGHT(108007,"客户已购买此课程"),
    COURSE_CAMP_NOT_EXIST(108008, "课程营期不存在"),
    COURSE_CAMP_AGENT_IS_EMPTY(108009, "课程营期二维码不存在"),
    COURSE_OFF_SHELF(108010, "课程已下架"),
    // 109 部门类错误
    DEPARTMENT_AGENT_IS_EMPTY(109001, "部门客服不存在"),

    // 110 支付宝支付错误
    ALI_PAY_FAILED(110001, "支付宝支付错误"),

    // 201 common
    COMMON_DOWNLOAD_FILE_ERROR(201001, "下载失败"),
    COMMON_DOWNLOAD_FILE_IS_EMPTY(201002, "文件为空"),
    FILE_SIZE_EXCEED_LIMIT(201003, "数据量超过{0}条"),

    // 301 短信
    COMMON_SMS_SEND_ERROR(301001, "短信推送失败"),
    PUSH_TIME_EXPIRED(301002, "当前时间超出消息设置的发送时间，请先重新调整发送时间"),

    // 400 会员中心
    FINANCIAL_RIGHTS_EXIST_MAPPING(400001, "金融权益已被关联，请先解除关联关系"),
    FINANCIAL_QUOTIENT_RIGHTS_EXIST_MAPPING(400002, "财商权益已被关联，请先解除关联关系"),
    FINANCIAL_TOOLS_EXIST_MAPPING(400003, "金融小工具已被关联，请先解除关联关系"),
    FINANCIAL_QUOTIENT_RIGHTS_NOT_EXIST(400004, "财商权益不存在"),


    //111 渠道
    CHANNEL_ALREADY_EXIST(111001,"渠道名已存在"),
    CHANNEL_NOT_EXIST(111002,"渠道不存在"),

    //112 预约活动
    APPOINTMENT_NOT_EXIST(112001,"预约活动不存在");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String description;

    /**
     * @param code        错误码
     * @param description 错误描述
     */
    ResponseStatusCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
