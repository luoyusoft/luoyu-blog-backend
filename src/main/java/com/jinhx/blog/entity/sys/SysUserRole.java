package com.jinhx.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinhx.blog.entity.base.BaseEntity;
import com.jinhx.blog.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 用户与角色对应关系
 * </p>
 *
 * @author luoyu
 * @since 2018-10-19
 */
@Data
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUserRole对象", description="用户与角色对应关系")
public class SysUserRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class})
    private Integer userId;

    @ApiModelProperty(value = "角色id")
    @NotNull(message = "角色id不能为空", groups = {AddGroup.class})
    private Integer roleId;

}
