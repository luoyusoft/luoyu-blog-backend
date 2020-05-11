package com.jinhaoxun.blog.project.tt.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.tt.pojo.shiro.UserPermission;
import com.jinhaoxun.blog.project.tt.dao.shiromapper.UserPermissionMapper;
import com.jinhaoxun.blog.project.tt.service.shiroservice.IUserPermissionService;
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
