package com.luoyu.blog.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DateUtils
 *
 * @author luoyu
 * @date 2018/10/20 13:26
 * @description 格式工具类
 */
public class FormatUtils {

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
