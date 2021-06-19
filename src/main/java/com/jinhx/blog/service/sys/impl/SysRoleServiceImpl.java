package com.jinhx.blog.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.common.util.SysAdminUtils;
import com.jinhx.blog.entity.sys.SysRole;
import com.jinhx.blog.mapper.sys.SysRoleMapper;
import com.jinhx.blog.service.sys.SysRoleMenuService;
import com.jinhx.blog.service.sys.SysRoleService;
import com.jinhx.blog.service.sys.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SysRoleServiceImpl
 * @author jinhx
 * @date 2018/10/25 15:36
 * @description
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取角色列表
     * @param page 页码
     * @param limit 页数
     * @param roleName 角色名
     * @return 角色列表
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String roleName) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));

        IPage<SysRole> rolePage = baseMapper.selectPage(new Query<SysRole>(params).getPage(),
                new QueryWrapper<SysRole>().lambda()
                .like(StringUtils.isNotBlank(roleName), SysRole::getRoleName,roleName)
        );

        return new PageUtils(rolePage);
    }

    /**
     * 根据角色id列表批量删除角色
     * @param roleIds 角色id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] roleIds) {
        if (SysAdminUtils.isHaveSuperAdmin(Arrays.asList(roleIds))){
            throw new MyException(ResponseEnums.NO_AUTH.getCode(), "超级管理员角色不可以删除");
        }
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatchByRoleId(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatchByRoleId(roleIds);
    }

    /**
     * 查询角色列表
     * @param createrId 创建者id
     * @return 角色列表
     */
    @Override
    public List<Integer> queryRoleIdList(Integer createrId) {
        return baseMapper.queryRoleIdList(createrId) ;
    }

    /**
     * 新增角色信息
     * @param sysRole 角色信息
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSysRole(SysRole sysRole) {
        baseMapper.insert(sysRole);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(sysRole.getId(), sysRole.getMenuIdList());
        return true;
    }

    /**
     * 更新角色信息
     * @param sysRole 角色信息
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysRoleById(SysRole sysRole){
        baseMapper.updateById(sysRole);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(sysRole.getId(), sysRole.getMenuIdList());
        return true;
    }

}
