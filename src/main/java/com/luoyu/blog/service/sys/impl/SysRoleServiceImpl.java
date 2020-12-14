package com.luoyu.blog.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.SysConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.entity.sys.SysRole;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.mapper.sys.SysRoleMapper;
import com.luoyu.blog.service.sys.SysRoleMenuService;
import com.luoyu.blog.service.sys.SysRoleService;
import com.luoyu.blog.service.sys.SysUserRoleService;
import com.luoyu.blog.service.sys.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * SysRoleServiceImpl
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 分页查询角色
     * @param page
     * @param limit
     * @param roleName
     * @param createUserId
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String roleName, Integer createUserId) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("roleName", roleName);
        params.put("createUserId", String.valueOf(createUserId));

        IPage<SysRole> rolePage = baseMapper.selectPage(new Query<SysRole>(params).getPage(),
                new QueryWrapper<SysRole>().lambda()
                .like(StringUtils.isNotBlank(roleName), SysRole::getRoleName,roleName)
                .eq(createUserId!=null, SysRole::getCreateUserId,createUserId)
        );
        return new PageUtils(rolePage);
    }

    @Override
    public void deleteBatch(Integer[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatchByRoleId(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatchByRoleId(roleIds);
    }

    /**
     * 查询roleId
     *
     * @param createUserId
     * @return
     */
    @Override
    public List<Integer> queryRoleIdList(Integer createUserId) {
        return baseMapper.queryRoleIdList(createUserId) ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysRole role) {
        role.setCreateTime(LocalDateTime.now());
        baseMapper.insert(role);

        //检查权限是否越权
        this.checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysRole role){
        baseMapper.updateById(role);

        //检查权限是否越权
        this.checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
        return true;
    }


    /**
     * 检查权限是否越权
     */
    private void checkPrems(SysRole role){
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if(SysConstants.SUPER_ADMIN.equals(role.getCreateUserId())){
            return ;
        }

        //查询用户所拥有的菜单列表
        List<Integer> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());

        //判断是否越权
        if(!menuIdList.containsAll(role.getMenuIdList())){
            throw new MyException(ResponseEnums.NO_AUTH.getCode(), "新增角色的权限，已超出你的权限范围");
        }
    }

}
