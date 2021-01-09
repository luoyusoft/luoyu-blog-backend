package com.luoyu.blog.common.util;

import com.luoyu.blog.common.config.params.ParamsHttpServletRequestWrapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HttpContextUtils
 *
 * @author luoyu
 * @date 2018/10/08 19:13
 * @description Http上下文
 */
public class HttpContextUtils {

	public static ParamsHttpServletRequestWrapper getHttpServletRequest() {
		return (ParamsHttpServletRequestWrapper) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static String getDomain(){
		ParamsHttpServletRequestWrapper request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	public static String getOrigin(){
		ParamsHttpServletRequestWrapper request = getHttpServletRequest();
		return request.getHeader("Origin");
	}

}
