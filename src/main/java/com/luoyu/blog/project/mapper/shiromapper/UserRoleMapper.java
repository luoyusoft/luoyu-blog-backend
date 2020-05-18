package com.luoyu.blog.project.mapper.shiromapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.project.pojo.shiro.UserRole;

import java.util.Set;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户角色Mapper接口
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * @author jinhaoxun
     * @description 获得用户角色列表
     * @param userId 用户id
     * @return ResponseResult 用户角色集合
     */
    Set<String> selectRoleSet(Long userId);

}
