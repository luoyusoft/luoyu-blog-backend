package com.jinhaoxun.blog.common.com.util.encodeutil;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description MD5工具类
 */
public class Md5Util {

    /**
     * 自定义UTF_8
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * @author jinhaoxun
     * @description 进行加密方法
     * @param str 传入未加密字段
     * @return String
     * @throws NoSuchAlgorithmException,UnsupportedEncodingException
     */
    public static String encoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5= getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes(UTF_8)));
        byte[] byteData = md5.digest();
        //将字节转换为十六进制格式方法一
        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * @author jinhaoxun
     * @description 判断用户密码是否正确方法
     * @param password 用户输入的密码
     * @param oldpassword 正确密码
     * @return Boolean
     * @throws NoSuchAlgorithmException,UnsupportedEncodingException
     */
    public static Boolean checkPasswordByMd5(String password,String oldpassword) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return encoderByMd5(password).equals(oldpassword);
    }
}
