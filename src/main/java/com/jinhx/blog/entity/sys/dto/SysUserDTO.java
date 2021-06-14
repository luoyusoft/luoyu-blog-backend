package com.jinhx.blog.entity.sys.dto;

import com.jinhx.blog.entity.sys.SysUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SysUserDTO
 * @author jinhx
 * @since 2018-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUserDTO对象", description="用户管理")
public class SysUserDTO extends SysUser {

    private List<Integer> roleIdList;

    private String roleNameStr;

}
