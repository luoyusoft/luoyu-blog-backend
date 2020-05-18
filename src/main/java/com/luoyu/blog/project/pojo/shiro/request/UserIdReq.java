package com.luoyu.blog.project.pojo.shiro.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户id请求实体类
 */
@Setter
@Getter
@ToString
public class UserIdReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID(主键)
     */
    private Long userId;

}
