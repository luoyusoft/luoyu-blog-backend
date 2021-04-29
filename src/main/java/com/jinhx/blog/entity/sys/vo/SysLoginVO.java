package com.jinhx.blog.entity.sys.vo;

import lombok.Data;

/**
 * SysLoginVO
 *
 * @author luoyu
 * @date 2018/10/20 14:51
 * @description 登录表单对象
 */
@Data
public class SysLoginVO {

    private String username;
    private String password;
    private String captcha;
    private String uuid;

}
