package com.jinhx.blog.service.sys;


import com.jinhx.blog.entity.sys.SysUser;
import com.jinhx.blog.entity.sys.SysUserToken;
import com.jinhx.blog.entity.sys.dto.SysUserDTO;

import java.util.Set;

/**
 * ShiroService
 *
 * @author luoyu
 * @date 2018/10/08 19:58
 * @description service接口类
 */
public interface ShiroService {

    /**
     * 获取用户的所有权限
     * @param userId
     * @return
     */
    Set<String> getUserPermissions(Integer userId);

    /**
     * 根据token查询token用户信息
     * @param token token
     * @return token用户信息
     */
    SysUserToken getSysUserTokenByToken(String token);

    /**
     * 根据用户id获取SysUserDTO
     * @param userId 用户id
     * @return SysUserDTO
     */
    SysUser getSysUserDTOByUserId(Integer userId);

    /**
     * 续期token
     * @param userId 用户id
     * @param accessToken 新token
     */
    void refreshToken(Integer userId, String accessToken);

}
