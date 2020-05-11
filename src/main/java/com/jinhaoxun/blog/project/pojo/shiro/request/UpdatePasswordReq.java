package com.jinhaoxun.blog.project.tt.pojo.shiro.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 更新密码请求实体类
 */
@Setter
@Getter
@ToString
public class UpdatePasswordReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

}
