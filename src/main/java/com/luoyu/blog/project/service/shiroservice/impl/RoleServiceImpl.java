package com.luoyu.blog.project.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.pojo.shiro.Role;
import com.luoyu.blog.project.service.shiroservice.IRoleService;
import com.luoyu.blog.project.mapper.shiromapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 角色服务类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
