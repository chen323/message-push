package com.chen.msgpush.constant;

import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * 系统内部非业务相关的常量。
 *
 * @author chen
 */
public class SystemConstant {

    private SystemConstant(){
    }

    /**
     * 下划线分隔符
     */
    public static final String UNDERLINE_SEPARATOR = "_";

    /**
     * 冒号分隔符
     */
    public static final String COLON_SEPARATOR = ":";

    /**
     * 百分号
     */
    public static final String PERCENT_SYMBOL = "%";

    /**
     * 点分隔符
     */
    public static final String DOT_SEPARATOR = ".";

    /**
     * 逗号分隔符
     */
    public static final String COMMA_SEPARATOR = ",";

    /**
     * 单引号分隔符
     */
    public static final String SINGLE_QUOTE_SEPARATOR = "'";

    /**
     * at分隔符
     */
    public static final String AT_SEPARATOR = "@";

    /**
     * 空格分隔符
     */
    public static final String SPACE_SEPARATOR = " ";

    /**
     * 回车分隔符
     */
    public static final String SEMICOLON_SEPARATOR = ";";

    /**
     * null字符串
     */
    public static final String NULL = "null";

    /**
     * 会员等级标识
     */
    public static final String V = "V";

    /**
     * 数字格式化，千分位口号，小数点后0位
     */
    public static final DecimalFormat zeroDecimalFormat = new DecimalFormat("#,###");
    /**
     * 数字格式化，千分位逗号，小数点后一位
     */
    public static final DecimalFormat oneDecimalFormat = new DecimalFormat("#,##0.0");
    /**
     * 数字格式化，千分位逗号，小数点后两位
     */
    public static final DecimalFormat twoDecimalFormat = new DecimalFormat("#,##0.00");

    /**
     * 数字格式化，千分位逗号，小数点后四位
     */
    public static final DecimalFormat fourDecimalFormat = new DecimalFormat("#,##0.0000");

    public static String getFormatMsg(String msg, Object... objects) {
        if (objects == null || objects.length == 0) {
            return msg;
        }
        MessageFormat mf = new MessageFormat(msg);
        return mf.format(objects);
    }
}
