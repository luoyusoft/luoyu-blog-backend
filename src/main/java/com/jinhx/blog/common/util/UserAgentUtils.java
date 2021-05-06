package com.jinhx.blog.common.util;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @author luoyu
 * @version 1.0
 * @parameter
 */
@Slf4j
public class UserAgentUtils {
    
    /**
     * 根据http获取userAgent信息
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 根据request获取userAgent，然后解析出osVersion
     * @param request
     * @return
     */
    public static String getOsVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)) {
            return null;
        }

        return getOsVersion(userAgent);
    }

    /**
     * 根据userAgent解析出osVersion
     * @param userAgent
     * @return
     */
    public static String getOsVersion(String userAgent) {
        if(StringUtils.isBlank(userAgent)){
            return null;
        }

        int start = userAgent.indexOf("(");
        int end = userAgent.indexOf(")");
        if (start < 0 || end < 0){
            return null;
        }
        String[] strArr = userAgent.substring(start + 1, end).split(";");
        if(strArr.length == 0){
            return null;
        }

        return strArr[1];
    }

    /**
     * 获取操作系统对象
     * @param userAgent
     * @return
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        return UserAgent.parseUserAgentString(userAgent).getOperatingSystem();
    }

    /**
     * 获取os：Windows/ios/Android
     * @param request
     * @return
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getOs(userAgent);
    }

    /**
     * 获取os：Windows/ios/Android
     * @param userAgent
     * @return
     */
    public static String getOs(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        if (operatingSystem == null){
            return null;
        }

        return operatingSystem.getGroup().getName();
    }

    /**
     * 获取deviceType
     * @param request
     * @return
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getDeviceType(userAgent);
    }

    /**
     * 获取deviceType
     * @param userAgent
     * @return
     */
    public static String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        if (operatingSystem == null){
            return null;
        }

        if (operatingSystem.getDeviceType() != null){
            return operatingSystem.getDeviceType().toString();
        }

        return null;
    }

    /**
     * 获取操作系统的名字
     * @param request
     * @return
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字
     * @param userAgent
     * @return
     */
    public static String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        if (operatingSystem == null){
            return null;
        }

        return operatingSystem.getName();
    }

    /**
     * 获取device的生产厂家
     * @param request
     */
    public static String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     * @param userAgent
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        if (operatingSystem == null){
            return null;
        }

        if (operatingSystem.getManufacturer() != null){
            return operatingSystem.getManufacturer().toString();
        }

        return null;
    }

    /**
     * 获取浏览器对象
     * @param agent
     * @return
     */
    public static Browser getBrowser(String agent) {
        return UserAgent.parseUserAgentString(agent).getBrowser();
    }

    /**
     * 获取browser name
     * @param request
     * @return
     */
    public static String getBorderName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBorderName(userAgent);
    }

    /**
     * 获取browser name
     * @param userAgent
     * @return
     */
    public static String getBorderName(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        return browser.getName();
    }

    /**
     * 获取浏览器的类型
     * @param request
     * @return
     */
    public static String getBorderType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBorderType(userAgent);
    }

    /**
     * 获取浏览器的类型
     * @param userAgent
     * @return
     */
    public static String getBorderType(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        return browser.getBrowserType().getName();
    }

    /**
     * 获取浏览器组： CHROME、IE
     * @param request
     * @return
     */
    public static String getBorderGroup(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBorderGroup(userAgent);
    }

    /**
     * 获取浏览器组： CHROME、IE
     * @param userAgent
     * @return
     */
    public static String getBorderGroup(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        return browser.getGroup().getName();
    }

    /**
     * 获取浏览器的生产厂商
     * @param request
     * @return
     */
    public static String getBrowserManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBrowserManufacturer(userAgent);
    }

    /**
     * 获取浏览器的生产厂商
     * @param userAgent
     * @return
     */
    public static String getBrowserManufacturer(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        return browser.getManufacturer().getName();
    }

    /**
     * 获取浏览器使用的渲染引擎
     * @param request
     * @return
     */
    public static String getBorderRenderingEngine(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBorderRenderingEngine(userAgent);
    }

    /**
     * 获取浏览器使用的渲染引擎
     * @param userAgent
     * @return
     */
    public static String getBorderRenderingEngine(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        return browser.getRenderingEngine().name();
    }

    /**
     * 获取浏览器版本
     * @param request
     * @return
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (StringUtils.isBlank(userAgent)){
            return null;
        }

        return getBrowserVersion(userAgent);
    }

    /**
     * 获取浏览器版本
     * @param userAgent
     * @return
     */
    public static String getBrowserVersion(String userAgent) {
        Browser browser = getBrowser(userAgent);
        if (browser == null){
            return null;
        }

        if (browser.getVersion(userAgent) != null){
            return browser.getVersion( userAgent).toString();
        }

        return null;
    }


    public static void main(String[] args) {
		String androidUserAgent = "Mozilla/5.0 (Linux; Android 8.0; LON-AL00 Build/HUAWEILON-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044204 Mobile Safari/537.36 V1_AND_SQ_7.7.8_908_YYB_D QQ/7.7.8.3705 NetType/WIFI WebP/0.3.0 Pixel/1440";
		String iosUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 QQ/7.7.8.421 V1_IPH_SQ_7.7.8_1_APP_A Pixel/750 Core/UIWebView Device/Apple(iPhone 6s) NetType/WIFI QBWebViewType/1";
        String winUserAgent = "\"Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";

        System.out.println("浏览器组：" + getBorderGroup(winUserAgent));
        System.out.println("浏览器名字：" + getBorderName(winUserAgent));
        System.out.println("浏览器类型：" + getBorderType(winUserAgent));
        System.out.println("浏览器生产商：" + getBrowserManufacturer(winUserAgent));
        System.out.println("浏览器版本：" + getBrowserVersion(winUserAgent));
        System.out.println("设备生产厂商：" + getDeviceManufacturer(winUserAgent));
        System.out.println("设备类型：" + getDeviceType(winUserAgent));
        System.out.println("设备操作系统：" + getOs(winUserAgent));
        System.out.println("操作系统的名字：" + getOsName(winUserAgent));
        System.out.println("操作系统的版本号：" + getOsVersion(winUserAgent));
        System.out.println("操作系统浏览器的渲染引擎：" + getBorderRenderingEngine(winUserAgent));
    }

}
