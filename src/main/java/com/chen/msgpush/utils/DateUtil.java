package com.chen.msgpush.utils;

import com.chen.msgpush.constant.ResponseStatusCode;
import com.chen.msgpush.exception.ApiBussException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 日期工具类
 *
 * @author chen
 */

@Slf4j
public class DateUtil {

    /**
     * 年-月-日 时:分:秒 毫秒
     */
    public static final String fmt_FullTime = "yyyyMMddHHmmssSSS";

    /**
     * 年-月-日 时:分:秒 毫秒
     */
    public static final String fmt_yMdHmsSTime = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * 年-月-日 时:分:秒,时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String fmt_yMdHmsTime = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-日 时:分 ,时间格式：yyyy-MM-dd HH:mm
     */
    public static final String fmt_yMdHmTime = "yyyy-MM-dd HH:mm";

    /**
     * 月-日 时:分 ,时间格式：MM-dd HH:mm
     */
    public static final String fmt_MdHmTime = "MM-dd HH:mm";
    /**
     * 年-月-日,时间格式：yyyy-MM-dd
     */
    public static final String fmt_yMdTime = "yyyy-MM-dd";

    /**
     * 年-月
     */
    public static final String fmt_YMTime = "yyyy-MM";

    /**
     * 月-日
     */
    public static final String fmt_MdTime = "MM-dd";

    /**
     * 年月日
     */
    public static final String fmt_yMdDate = "yyyyMMdd";
    /**
     * 年月
     */
    public static final String fmt_yMDate = "yyyyMM";

    /**
     * 年.月.日
     */
    public static final String fmt_yMdDot = "yyyy.MM.dd";

    /**
     * 时分秒
     */
    public static final String fmt_Onlytime = "HH:mm:ss";

    public static final String fmt_yMCN = "yyyy年MM月";

    public static final String fmt_yMdHCN = "yyyy年MM月dd日 HH点mm分";


    /**
     * 日
     */
    public static final int INTERVAL_DAY = 1;

    /**
     * 周
     */
    public static final int INTERVAL_WEEK = 2;

    /**
     * 月
     */
    public static final int INTERVAL_MONTH = 3;

    /**
     * 年
     */
    public static final int INTERVAL_YEAR = 4;

    /**
     * 小时
     */
    public static final int INTERVAL_HOUR = 5;

    /**
     * 分钟
     */
    public static final int INTERVAL_MINUTE = 6;

    /**
     * 秒
     */
    public static final int INTERVAL_SECOND = 7;

    public static int getTenLengthTimeStampByDate(Date date) {
        long timestamp = date.getTime();
        return (int) (timestamp / 1000);
    }

    public static Date getYesterday() {
        Date date = new Date();
        long time = (date.getTime() / 1000) - 60 * 60 * 24;
        date.setTime(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat(fmt_yMdTime);
        try {
            date = format.parse(format.format(date));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return date;
    }

    /**
     * 字符型转换成日期型
     *
     * @param date       日期
     * @param dateFormat 日期格式
     * @return 格式化后的日期
     */
    public static Date stringDateFormat(String date, String dateFormat) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(),
                    "日期转换错误");
        }
    }

    /**
     * 增加时间
     *
     * @param interval [INTERVAL_DAY,INTERVAL_WEEK,INTERVAL_MONTH,INTERVAL_YEAR,
     *                 INTERVAL_HOUR,INTERVAL_MINUTE]
     * @param date     日期
     * @param n        可以为负数
     * @return 更新后的日期
     */
    public static Date dateAdd(int interval, Date date, int n) {
        if (date == null) {
            return null;
        }
        long time = (date.getTime() / 1000); // 单位秒
        switch (interval) {
            case INTERVAL_DAY:
                time = time + (long) n * 60 * 60 * 24;
                break;
            case INTERVAL_WEEK:
                time = time + (long) n * 60 * 60 * 24 * 7;
                break;
            case INTERVAL_MONTH:
                time = time + (long) n * 60 * 60 * 24 * 31;
                break;
            case INTERVAL_YEAR:
                time = time + (long) n * 60 * 60 * 24 * 365;
                break;
            case INTERVAL_HOUR:
                time = time + (long) n * 60 * 60;
                break;
            case INTERVAL_MINUTE:
                time = time + n * 60L;
                break;
            case INTERVAL_SECOND:
                time = time + n;
                break;
            default:
        }

        Date result = new Date();
        result.setTime(time * 1000);
        // 最大年不超过9999-12-31
        Calendar resultCalendar = Calendar.getInstance();
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.YEAR, 9998);
        maxCalendar.set(Calendar.MONTH, 12);
        maxCalendar.set(Calendar.DATE, 31);
        resultCalendar.setTime(result);
        if (result.getTime() > maxCalendar.getTimeInMillis()) {
            return maxCalendar.getTime();
        } else {
            return result;
        }
    }

    /**
     * 日期型转换成字符串
     *
     * @param date       日期
     * @param dateFormat 日期格式
     * @return 格式化后的日期字符串
     */
    public static String dateFormat(Date date, String dateFormat) {
        if (Objects.isNull(date)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 日期1和日期2相差的天数
     */
    public static Integer daysBetweenDate(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 获取两个日期相差的天数 不计算时分秒
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 日期1和日期2相差的天数，不包括时分秒
     */
    public static Integer daysBetweenDateWithoutHms(Date date1, Date date2) {
        // 去除时分秒
        long date1Time = date1.getTime() - ((date1.getTime() + 28800000) % 86400000);
        long date2Time = date2.getTime() - ((date2.getTime() + 28800000) % 86400000);

        return (int) ((date2Time - date1Time) / (1000 * 3600 * 24));
    }

    /**
     * 设置当前时间为当天的最初时间（即00时00分00秒）
     *
     * @param date 日期
     * @return 给定日期的当天最初时间
     */
    public static Date setStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 设置当前时间为当前的最后时间（即23时59分59秒）
     *
     * @param date 日期
     * @return 给定日期的当天最后时间
     */
    public static Date setEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 设置当前时间为当月的最初时间（即00时00分00秒）
     *
     * @param date 日期
     * @return 给定日期的当天最初时间
     */
    public static Date setMonthStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 设置当前时间为当前的最后时间（即23时59分59秒）
     *
     * @param date 日期
     * @return 给定日期的当天最后时间
     */
    public static Date setMonthEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 比较两个日期相隔的秒数
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return date2-date1
     */
    public static Integer getDiffSeconds(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        long diff = cal2.getTime().getTime() - cal1.getTime().getTime();
        double value = diff * 1.0 / 1000;
        return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * localDateTime转string
     */
    public static String getStringFromLocalDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDateTime);
    }

    public static List<Date> getMonthListByStartDateAndEndDate(Date startDate, Date endDate) {
        List<Date> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (calendar.getTime().before(endDate)) {
            calendar.add(Calendar.MONTH, 1);
            result.add(calendar.getTime());
        }

        return result;
    }

    public static Date strToDate(String s) {
        Date d = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = sdf.parse(s);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return d;
    }
}
