package com.jinhx.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinhx.blog.entity.base.BaseEntity;
import com.jinhx.blog.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author luoyu
 * @since 2018-10-19
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="角色")
public class SysRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名称不能为空", groups = {AddGroup.class})
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist=false)
    private List<Integer> menuIdList;

}
