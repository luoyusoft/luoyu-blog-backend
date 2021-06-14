package com.jinhx.blog.service.sys;


import java.awt.image.BufferedImage;

/**
 * SysCaptchaService
 *
 * @author luoyu
 * @date 2018/10/19 18:52
 * @description 验证码类
 */
public interface SysCaptchaService {

    /**
     * 获取验证码
     * @param uuid uuid
     * @return 验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证验证码
     * @param uuid uuid
     * @param code 验证码
     * @return 校验结果
     */
    boolean validate(String uuid, String code);

}
