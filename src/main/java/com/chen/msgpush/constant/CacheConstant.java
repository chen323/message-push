package com.chen.msgpush.constant;

/**
 * 缓存相关常量。
 *
 * @author chen
 */
public class CacheConstant {

    private CacheConstant() {
    }

    /**
     * 日缓存失效时间，7天，单位是秒,对应properties文件key
     */
    public static final String DAY_SEVEN_DURATION = "day.7.duration";

    /**
     * 日缓存失效时间，1天，单位是秒,对应properties文件key
     */
    public static final String DAY_DURATION = "day.duration";

    /**
     * 半天缓存失效时间，0.5天（换算单位：秒）,对应properties文件key
     */
    public static final String HALF_DAY_DURATION = "half.day.duration";

    /**
     * 分钟缓存失效时间，30分钟（换算单位：秒）,
     * 对应properties文件key
     */
    public static final String THIRTY_MINUTE_DURATION = "minute.30.duration";

    /**
     * 分钟缓存失效时间，10分钟，单位是秒,对应properties文件key
     */
    public static final String MINUTE_TEN_DURATION = "minute.10.duration";

    /**
     * 分钟缓存失效时间，4分钟，单位是秒
     */
    public static final String MINUTE_FOUR_DURATION = "minute.4.duration";

    /**
     * 分钟缓存失效时间，5分钟，单位是秒,对应properties文件key
     */
    public static final String MINUTE_FIVE_DURATION = "minute.5.duration";

    /**
     * 分钟缓存失效时间，1分钟，单位是秒,对应properties文件key
     */
    public static final String MINUTE_DURATION = "minute.duration";

    /**
     * 小时缓存失效时间，60分钟，单位是秒,对应properties文件key
     */
    public static final String HOUR_DURATION = "hour.duration";

    /**
     * 小时缓存失效时间，120分钟，单位是秒,对应properties文件key
     */
    public static final String HOUR_TWO_DURATION = "hour.2.duration";

    /**
     * 用户类缓存通用前缀
     */
    public static final String LOGIN_CACHE_PREFIX = "login";

    /**
     * 用户IP地址刷新次数缓存
     */
    private static final String LOGIN_IP_VISIT_TIMES_CACHE_PREFIX = LOGIN_CACHE_PREFIX + "_ip_visit_times";

    /**
     * 用户ip访问次数缓存key
     *
     * @param ip       ip地址
     * @param username 用户姓名
     * @return 用户ip访问次数缓存key
     */
    public static String getIpVisitTimesKey(String ip, String username) {
        return CacheConstant.LOGIN_IP_VISIT_TIMES_CACHE_PREFIX + SystemConstant.UNDERLINE_SEPARATOR + ip + SystemConstant.UNDERLINE_SEPARATOR + username;
    }
}
