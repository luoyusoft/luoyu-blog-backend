package com.jinhaoxun.blog.project.tt.pojo.shiro.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户登录请求实体类
 */
@Setter
@Getter
@ToString
public class UserLoginReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private String type;

}
