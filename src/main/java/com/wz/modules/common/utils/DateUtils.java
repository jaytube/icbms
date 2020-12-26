package com.wz.modules.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类DateUtils的功能描述: 日期处理
 *
 * @auther hxy
 * @date 2017-08-25 16:12:36
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_TIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.S";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String formatMS(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_MS_PATTERN);
            return df.format(date);
        }
        return null;
    }

    public static String getPreHour(String str, int hour) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        Date date1 = new Date(calendar.getTimeInMillis());
        return sdf.format(date1);
    }

    public static String getPreDay(String str, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        Date date1 = new Date(calendar.getTimeInMillis());
        return sdf.format(date1);
    }

    /**
     * 根据秒数获取时间串
     *
     * @param second (eg: 100s)
     * @return (eg : 00 : 01 : 40)
     */
    public static String getTimeStrBySecond(long second) {

        long day = second / 60 / 60 / 24;
        long hour = second / 60 / 60 % 24;
        long minute = second / 60 % 60;
        long seconds = second % 60;

        StringBuilder sb = new StringBuilder();
        if (day != 0) {
            sb.append(day).append("天");
        }
        if (hour != 0) {
            sb.append(hour).append("小时");
        }
        if (minute != 0) {
            sb.append(minute).append("分");
        }
        if (seconds != 0) {
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    public static int getExpire(int expire, String unit) {
        int e = expire;
        if ("seconds".equalsIgnoreCase(unit)) {
            e = expire;
        }
        if ("minute".equalsIgnoreCase(unit)) {
            e = expire * 60;
        }
        if ("hours".equalsIgnoreCase(unit)) {
            e = expire * 60 * 60;
        }
        if ("days".equalsIgnoreCase(unit)) {
            e = (expire * 60 * 60) * 24;
        }
        return e;
    }
}