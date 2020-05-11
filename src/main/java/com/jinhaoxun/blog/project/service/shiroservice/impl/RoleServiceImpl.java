package com.jinhaoxun.blog.project.tt.service.shiroservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.tt.pojo.shiro.Role;
import com.jinhaoxun.blog.project.tt.dao.shiromapper.RoleMapper;
import com.jinhaoxun.blog.project.tt.service.shiroservice.IRoleService;
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
