package com.luoyu.blog.project.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.pojo.shiro.Permission;
import com.luoyu.blog.project.service.shiroservice.IPermissionService;
import com.luoyu.blog.project.mapper.shiromapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 权限服务类
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
