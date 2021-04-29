package com.jinhx.blog.common.util;

import org.apache.commons.codec.binary.Hex;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 加密工具类
 */
public class EncodeUtils {

    /**
     * @author jinhaoxun
     * @description 进行加密方法
     * @param str 传入未加密字段
     * @return String
     */
    public static String encoderByBcrypt(String str){
        return BCrypt.hashpw(str, BCrypt.gensalt());
    }

    /**
     * @author jinhaoxun
     * @description 判断用户密码是否正确方法
     * @param str 用户输入的密码
     * @param oldpassword 正确密码
     * @return Boolean
     */
    public static boolean checkByBcrypt(String str, String oldpassword){
        return BCrypt.checkpw(str, oldpassword);
    }

    /**
     * @author jinhaoxun
     * @description 进行加密方法
     * @param str 传入未加密字段
     * @return String
     */
    public static String encoderByMD5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] bytes = md.digest(str.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @author jinhaoxun
     * @description 判断用户密码是否正确方法
     * @param str 用户输入的密码
     * @param oldpassword 正确密码
     * @return Boolean
     */
    public static boolean checkByMD5(String str, String oldpassword) throws Exception {
        return oldpassword.equals(encoderByMD5(str));
    }

    /**
     * @author jinhaoxun
     * @description 进行加密方法
     * @param str 传入未加密字段
     * @return String
     */
    public static String encoderBySHA256(String str) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
        return Hex.encodeHexString(hash);
    }

    /**
     * @author jinhaoxun
     * @description 判断用户密码是否正确方法
     * @param password 用户输入的密码
     * @param oldpassword 正确密码
     * @return Boolean
     */
    public static boolean checkBySHA256(String password, String oldpassword) throws Exception {
        return oldpassword.equals(encoderBySHA256(password));
    }

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    /**
     * @Author: jinhaoxun
     * @Description: AES 加密操作
     * @param content 待加密内容
     * @param key 加密密钥
     * @Date: 2020/4/2 上午12:46
     * @Return: javax.crypto.spec.SecretKeySpec 返回Base64转码后的加密数据
     * @Throws:
     */
    public static String encryptByAES(String content, String key) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return new String(Base64Utils.encode(result));//通过Base64转码返回
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: jinhaoxun
     * @Description: AES 解密操作
     * @param content
     * @param key
     * @Date: 2020/4/2 上午12:46
     * @Return: javax.crypto.spec.SecretKeySpec
     * @Throws:
     */
    public static String decryptByAES(String content, String key) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decode(content.getBytes()));
            return new String(result, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: jinhaoxun
     * @Description: 生成加密秘钥
     * @param key
     * @Date: 2020/4/2 上午12:46
     * @Return: javax.crypto.spec.SecretKeySpec
     * @Throws:
     */
    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // 解决操作系统内部状态不一致问题（部分liunx不指定类型，无法解密）
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kg.init(128, secureRandom);

//            kg.init(128, new SecureRandom(key.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
