package com.jinhaoxun.blog.project.tt.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.tt.pojo.shiro.UserRole;
import com.jinhaoxun.blog.project.tt.dao.shiromapper.UserRoleMapper;
import com.jinhaoxun.blog.project.tt.service.shiroservice.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户角色服务类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
