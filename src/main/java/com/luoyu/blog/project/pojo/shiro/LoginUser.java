package com.luoyu.blog.project.pojo.shiro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 登录用户实体类
 */
@Setter
@Getter
@ToString
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    public Long userId;

    /**
     * 姓名
     */
    public String name;

    /**
     * @author jinhaoxun
     * @description 构造器
     */
    public LoginUser() {
    }

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param name 姓名
     */
    public LoginUser(String name) {
        this.name = name;
    }

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param userId 用户ID
     * @param name 姓名
     */
    public LoginUser(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}