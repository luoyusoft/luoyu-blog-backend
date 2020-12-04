package com.luoyu.blog.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.entity.sys.SysUserRole;

import java.util.List;

/**
 * SysUserRoleMapper
 *
 * @author luoyu
 * @date 2018/10/26 00:02
 * @description
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 查询roleId
     * @param userId
     * @return
     */
    List<Integer> queryRoleIdList(Integer userId);

}
