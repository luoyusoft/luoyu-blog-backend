package com.luoyu.blog.project.mapper.shiromapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.project.pojo.shiro.UserPermission;

import java.util.Set;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户权限Mapper接口
 */
public interface UserPermissionMapper extends BaseMapper<UserPermission> {

    /**
     * @author jinhaoxun
     * @description 获得用户权限列表
     * @param userId 用户id
     * @return ResponseResult 用户权限集合
     */
    Set<String> selectPermissionSet(Long userId);

}
