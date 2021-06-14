package com.jinhx.blog.common.auth;

import com.jinhx.blog.entity.sys.SysUser;
import com.jinhx.blog.entity.sys.SysUserToken;
import com.jinhx.blog.entity.sys.dto.SysUserDTO;
import com.jinhx.blog.service.sys.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * OAuth2Realm
 *
 * @author luoyu
 * @date 2018/10/07 16:39
 * @description Shiro 认证类
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserDTO sysUserDTO = (SysUserDTO) principals.getPrimaryPrincipal();
        Integer userId = sysUserDTO.getId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        // 根据accessToken，查询用户信息
        SysUserToken sysUserToken = shiroService.getSysUserTokenByToken(accessToken);
        // token失效
        if(sysUserToken == null){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        // 查询用户信息
        SysUser sysUser = shiroService.getSysUserDTOByUserId(sysUserToken.getUserId());
        // 账号锁定
        if(sysUser.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 续期token
        shiroService.refreshToken(sysUserToken.getUserId(),accessToken);

        return new SimpleAuthenticationInfo(sysUser, accessToken, getName());
    }

}
