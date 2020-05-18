package com.luoyu.blog.project.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.pojo.shiro.UserPermission;
import com.luoyu.blog.project.service.shiroservice.IUserPermissionService;
import com.luoyu.blog.project.mapper.shiromapper.UserPermissionMapper;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 用户权限服务类
 */
@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermission> implements IUserPermissionService {

}
