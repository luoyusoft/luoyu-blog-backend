package com.jinhx.blog.service.sys;

import com.jinhx.blog.entity.sys.SysUserToken;

/**
 * SysUserTokenService
 * @author jinhx
 * @date 2018/10/20 15:17
 * @description
 */
public interface SysUserTokenService {

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    String createToken(Integer userId);

    /**
     * 根据token查询token用户信息
     * @param token token
     * @return token用户信息
     */
    SysUserToken getSysUserTokenByToken(String token);

    /**
     * 退出登录
     * @param userId 用户id
     */
    void logout(Integer userId);

    /**
     * 续期token
     * @param userId 用户id
     * @param accessToken 新token
     */
    void refreshToken(Integer userId, String accessToken);

}
