package com.jinhaoxun.blog.project.tt.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.tt.pojo.shiro.Permission;
import com.jinhaoxun.blog.project.tt.dao.shiromapper.PermissionMapper;
import com.jinhaoxun.blog.project.tt.service.shiroservice.IPermissionService;
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
