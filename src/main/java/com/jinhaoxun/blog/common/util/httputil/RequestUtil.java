package com.jinhaoxun.blog.common.com.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 请求工具类
 */
public class RequestUtil {

    /**
     * @author jinhaoxun
     * @description 获取当前登录用户id方法
     * @return String
     */
    public static String getUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getAttribute("userId").toString();
    }
}
