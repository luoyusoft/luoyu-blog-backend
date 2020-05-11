package com.jinhaoxun.blog.framework.tt.shiro.realm;

import com.jinhaoxun.accommon.constant.AbstractConstant;
import com.jinhaoxun.blog.common.exception.CustomRuntimeException;
import com.jinhaoxun.blog.common.exception.ExceptionFactory;
import com.jinhaoxun.blog.common.response.ResponseMsg;
import com.jinhaoxun.blog.common.util.otherutil.JwtUtil;
import com.jinhaoxun.blog.project.dao.shiromapper.UserMapper;
import com.jinhaoxun.blog.project.dao.shiromapper.UserPermissionMapper;
import com.jinhaoxun.blog.project.dao.shiromapper.UserRoleMapper;
import com.jinhaoxun.blog.framework.tt.shiro.jwt.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 自定义Realm类
 */
@Component
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserPermissionMapper userPermissionMapper;
    @Resource
    private ExceptionFactory exceptionFactory;

    /**
     * @author jinhaoxun
     * @description 必须重写此方法，不然会报错
     * @param token token
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Jwt;
    }


    /**
     * @author jinhaoxun
     * @description 默认使用此方法进行用户名正确与否验证，错误抛出异常即可
     * @param authenticationToken token
     * @return AuthenticationInfo
     * @throws CustomRuntimeException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws RuntimeException {
        log.info("————  开始身份认证  ————");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得 userId，用于和数据库进行对比
        String userId = JwtUtil.getClaim(token, AbstractConstant.TOKEN_USER_ID);
        String password = userMapper.selectPassword(Long.valueOf(userId));
        if (password == null) {
            throw new CustomRuntimeException(ResponseMsg.USER_NOT_EXIST.getCode(), "用户不存在", "用户不存在");
        }
/*        // 开始认证，要 token 认证通过，且 Redis 中存在 refreshToken，且两个 token 时间戳一致
        if (JwtUtil.verify(token,username,password) && jedisutil.exists(CONSTANT.REFRESH_TOKEN_KEY.getMsg() + username)) {
            // 获取 refreshToken 的时间戳
            String currentTimeMillisRedis = jedisutil.getJson(CONSTANT.REFRESH_TOKEN_KEY.getMsg() + username);
            // 获取 token 时间戳，与 tefreshToken 的时间戳对比
            if (JwtUtil.getClaim(token, "currentTimeMillis").equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "CustomRealm");
            }
        }*/
        if (JwtUtil.verify(token,userId,password)) {
            return new SimpleAuthenticationInfo(token, token, getName());
        }
        throw new CustomRuntimeException(ResponseMsg.IDENTITY_INFORMATION_IS_EXPIRED.getCode(),
                ResponseMsg.IDENTITY_INFORMATION_IS_EXPIRED.getMsg(), ResponseMsg.IDENTITY_INFORMATION_IS_EXPIRED.getMsg());
    }

    /**
     * @author jinhaoxun
     * @description 只有当需要检测用户权限的时候才会调用此方法，例如 checkRole,checkPermission 之类的
     * @param principals
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("————  开始权限认证  ————");
        String userId = JwtUtil.getClaim(principals.toString(), AbstractConstant.TOKEN_USER_ID);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取该用户角色
        Set<String> roleSet = new HashSet<>();
        roleSet = userRoleMapper.selectRoleSet(Long.valueOf(userId));
        //获取该用户所有的权限
        Set<String> permissionSet = new HashSet<>();
        permissionSet = userPermissionMapper.selectPermissionSet(Long.valueOf(userId));
        /*
         * 需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
         * 设置该用户拥有的角色和权限
         */
        info.setRoles(roleSet);
        info.setStringPermissions(permissionSet);
        return info;
    }
}
