package com.jinhaoxun.blog.common.com.util.datautil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 时间计算工具类
 */
public class TimeUtil {

    /**
     * @author jinhaoxun
     * @description 时间秒数计算方法
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return int 秒数
     */
    public static int getSeconds(TimeUnit timeUnit, int duration) {
        return (int) timeUnit.toSeconds(duration);
    }

    /**
     * @author jinhaoxun
     * @description 时间秒数计算方法
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return long 毫秒数
     */
    public static long getMillis(TimeUnit timeUnit, int duration) {
        return timeUnit.toMillis(duration);
    }

    /**
     * @author jinhaoxun
     * @description 计算两个时间的时间差，单位秒
     * @param date 未来时间
     * @return int 秒数
     */
    public static int getTimeDifferenceSeconds(Date date,Date now) {
        return (int)((date.getTime() - now.getTime()) / 1000);
    }

    /**
     * @author jinhaoxun
     * @description 获取指定格式的时间字符串
     * @param date 转换的时间
     * @param format 转换的格式
     * @return String 转换后的数据
     */
    public static String getDateStringByFormat(Date date, String format){
        SimpleDateFormat simple = new SimpleDateFormat(format);
        return simple.format(date);
    }

    /**
     * @author jinhaoxun
     * @description 将当前时间向后移动1天的毫秒数
     * @param now 当前时间
     * @return Date 一天后的时间
     */
    public static Date addOneDay(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 将当前时间向后移动n天的毫秒数
     * @param now 当前时间
     * @return 一天后的时间
     */

    public static Date addDay(Date now, Integer n) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_MONTH, n);
        return c.getTime();
    }

    /**
     * @author jinhaoxun
     * @description 获取前一天的开始时间
     * @param date 设定的时间
     * @return Date 前一天的开始时间
     */
    public static Date getYesterdayBeginTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return getBeginTimeOfDay(calendar.getTime());
    }

    /**
     * @author jinhaoxun
     * @description 获取前一天的结束时间
     * @param date 设定的时间
     * @return Date 前一天的结束时间
     */
    public static Date getYesterdayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return getEndTimeOfDay(calendar.getTime());
    }

    /**
     * @author jinhaoxun
     * @description 获取零点时间
     * @param date 要获取的时间
     * @return Date 获取的时间
     */
    public static Date getBeginTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * @author jinhaoxun
     * @description 获取一天结束时间
     * @param date 要获取的时间
     * @return Date 获取的时间
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * @author jinhaoxun
     * @description 判断是否某天时间范围内
     * @param date 要判断的时间
     * @param start 开始时间
     * @param end 结束时间
     * @return boolean 返回是否在范围内
     * @throws ParseException
     */
    public static boolean isToday(Date date,Double start,Double end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        Date day = sdf.parse(time);
        Double beginTime = day.getTime()+start*3600*1000;
        Double endTime = day.getTime()+end*3600*1000;
        if(date.getTime()>beginTime&&date.getTime()<endTime) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * @author jinhaoxun
     * @description 判断当给定时间是否处于指定时间范围内
     * @param now 要判断的时间
     * @param start 开始时间
     * @param end 结束时间
     * @return boolean 返回是否在范围内
     */
    public static boolean nowBetween(Date now, Date start, Date end) {
        long nowTime = now.getTime();
        long beginTime = start.getTime();
        long endTime = end.getTime();
        return nowTime >= beginTime && nowTime <= endTime;
    }

    /**
     * @author jinhaoxun
     * @description 获取当前时间的前几分钟时间
     * @param n 第n分钟前
     * @return String 第n分钟前
     */
    public static String getBeforeTimeMinute(int n){
        Calendar beforeTime = Calendar.getInstance();
        // n分钟之前的时间
        beforeTime.add(Calendar.MINUTE, -n);
        Date beforeDate = beforeTime.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeDate);
    }
}