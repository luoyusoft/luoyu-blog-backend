package com.luoyu.blog.common.util.datautil;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description String工具类
 */
public class StringUtil {

    /**
     * 自定义下划线
     */
    private static final char UNDERLINE = '_';

    /**
     * 从字符串中找出数字(正则)
     */
    private static final Pattern GET_NUMBER_PATTERN = Pattern.compile("\\d+");

    /**
     * 是否是手机号(正则)
     */
    private static final Pattern IS_PHONE_PATTERN = Pattern.compile("^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$");

    /**
     * @author jinhaoxun
     * @description String为空判断(不允许空格)方法
     * @param str 要进行判断的数据
     * @return boolean 是否为空
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * @author jinhaoxun
     * @description String不为空判断(不允许空格)方法
     * @param str 要进行判断的数据
     * @return boolean 是否为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * @author jinhaoxun
     * @description Byte数组为空判断方法
     * @param bytes 要进行判断的数据
     * @return boolean 是否为空
     */
    public static boolean isNull(byte[] bytes){
        // 根据byte数组长度为0判断
        return bytes.length == 0;
    }

    /**
     * @author jinhaoxun
     * @description Byte数组不为空判断方法
     * @param bytes 要进行判断的数据
     * @return boolean 是否为空
     */
    public static boolean isNotNull(byte[] bytes) {
        return !isNull(bytes);
    }

    /**
     * @author jinhaoxun
     * @description 驼峰转下划线方法
     * @param param 要进行转换的数据
     * @return String 转换后的数据
     */
    public static String camelToUnderline(String param) {
        if (isNotBlank(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(UNDERLINE);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * @author jinhaoxun
     * @description 下划线转驼峰方法
     * @param param 要进行转换的数据
     * @return String 转换后的数据
     */
    public static String underlineToCamel(String param) {
        if (isNotBlank(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == 95) {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * @author jinhaoxun
     * @description 在字符串两周添加''方法
     * @param param 要进行转换的数据
     * @return String 转换后的数据
     */
    public static String addSingleQuotes(String param) {
        return "\'" + param + "\'";
    }

    /**
     * @author jinhaoxun
     * @description 星化手机号
     * @param str 要进行转换的数据
     * @return String 转换后的数据
     */
    public static String starFlyPhone(String str) {
        if (isPhone(str)) {
            return str.substring(0, 3) + "****" + str.substring(7);
        } else {
            return null;
        }
    }

    /**
     * @author jinhaoxun
     * @description 格式化字符串
     * @param str 要进行转换的数据
     * @return String 转换后的数据
     */
    public static String format(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * @author jinhaoxun
     * @description 正则校验
     * @param str 要进行校验的数据
     * @param pattern 规则
     * @return String 转换后的数据
     * @return boolean 是否正确
     */
    public static boolean matche(String str, Pattern pattern) {
        return !isEmpty(str) && pattern.matcher(str).matches();
    }

    /**
     * @author jinhaoxun
     * @description 是否是手机号
     * @param str 要进行判断的手机号
     * @return boolean 是否为手机号
     */
    public static boolean isPhone(String str) {
        return matche(str, IS_PHONE_PATTERN);
    }

    /**
     * @author jinhaoxun
     * @description 从字符串中找出数字
     * @param str 要进行查找的数据
     * @return String 找到的数字
     */
    public static String findNumber(String str) {
        Matcher matcher = GET_NUMBER_PATTERN.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /**
     * @author jinhaoxun
     * @description 获取随机不重复字符串
     * @param n 字符串长度
     * @return String 随机不重复字符串
     */
    public static String random(int n) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int itmp = r.nextInt(26) + 65;
            char ctmp = (char) itmp;
            sb.append(ctmp);
        }
        return System.currentTimeMillis() + sb.toString() + r.nextInt(100);
    }
}
