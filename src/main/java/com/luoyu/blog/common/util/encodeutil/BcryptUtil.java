package com.luoyu.blog.common.util.encodeutil;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description BCrypt工具类
 */
public class BcryptUtil {

    /**
     * @author jinhaoxun
     * @description 进行加密方法
     * @param password 传入未加密字段
     * @return String
     */
    public static String encoderByBcrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * @author jinhaoxun
     * @description 判断用户密码是否正确方法
     * @param password 用户输入的密码
     * @param oldpassword 正确密码
     * @return Boolean
     */
    public static Boolean checkPasswordByBcrypt(String password, String oldpassword){
        return BCrypt.checkpw(password, oldpassword);
    }
}
