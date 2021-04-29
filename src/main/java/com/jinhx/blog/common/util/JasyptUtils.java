package com.jinhx.blog.common.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class JasyptUtils {

    public static void main(String[] args) {
        //加密工具
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");//加密方式，默认PBEWithMD5AndDES，可改PBEWithMD5AndTripleDES
        config.setPassword("");//加密所需的salt(盐)
        //应用配置
        encryptor.setConfig(config);

        //加密
        encryption(encryptor, "");
        //解密
        decrypt(encryptor, "");
    }

    /**
     * Jasypt加密 结果
     * @param encryptor 加密工具
     * @param plaintext 需要加密字符串
     */
    public static void encryption(StandardPBEStringEncryptor encryptor, String plaintext){
        //加密
        String ciphertext = encryptor.encrypt(plaintext);
        System.out.println(plaintext + " : " + ciphertext);
    }

    /**
     * Jasypt解密 结果
     * @param encryptor 解密工具
     * @param ciphertext 需要解密字符串
     */
    public static void decrypt(StandardPBEStringEncryptor encryptor, String ciphertext){
        //解密
        String plaintext = encryptor.decrypt(ciphertext);
        System.out.println(ciphertext + " : " + plaintext);
    }

}
