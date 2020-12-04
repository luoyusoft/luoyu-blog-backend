package com.luoyu.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.common.validator.group.UpdateGroup;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 用户管理
 * </p>
 *
 * @author luoyu
 * @since 2018-10-08
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="用户管理")
public class SysUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空" , groups = {AddGroup.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空" ,groups = AddGroup.class)
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message="邮箱不能为空", groups = {AddGroup.class})
    @Email(message="邮箱格式不正确", groups = {AddGroup.class})
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    @ApiModelProperty(value = "创建者id")
    @Email(message="创建者id不能为空", groups = {AddGroup.class})
    private Integer createUserId;

    @ApiModelProperty(value = "用户状态（0：禁用，1：正常）")
    private Integer status;

    @TableField(exist=false)
    private List<Integer> roleIdList;

}
