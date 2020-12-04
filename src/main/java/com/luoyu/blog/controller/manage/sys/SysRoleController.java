package com.luoyu.blog.controller.manage.sys;

import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.base.AbstractController;
import com.luoyu.blog.common.constants.SysConstants;
import com.luoyu.blog.entity.sys.SysRole;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.service.sys.SysRoleMenuService;
import com.luoyu.blog.service.sys.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SysRoleController
 *
 * @author luoyu
 * @date 2018/10/25 15:32
 * @description
 */
@RestController
@RequestMapping("/admin/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 分页查询角色列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("roleName") String roleName){
        //如果不是超级管理员，则只查询自己创建的角色列表
        Integer createUserId = null;
        if(!SysConstants.SUPER_ADMIN.equals(getUserId())){
            createUserId = getUserId();
        }

        PageUtils rolePage = sysRoleService.queryPage(page, limit, roleName, createUserId);

        return Response.success(rolePage);
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public Response select(){
        Map<String, Object> map = new HashMap<>();

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if(!SysConstants.SUPER_ADMIN.equals(getUserId())){
            map.put("createUserId", getUserId());
        }
        Collection<SysRole> list = sysRoleService.listByMap(map);
        return Response.success(list);
    }

    /**
     * 保存角色信息
     * @param role
     * @return
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public Response save(@RequestBody SysRole role){
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.save(role);

        return Response.success();
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:role:update")
    public Response update(@RequestBody SysRole role){
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.updateById(role);

        return Response.success();
    }

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public Response info(@PathVariable("roleId") Integer roleId){
        SysRole role = sysRoleService.getById(roleId);
        List<Integer> menuIdList=sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        return Response.success(role);
    }

    /**
     * 删除角色信息
     * @param roleIds
     * @return
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Response delete(@RequestBody Integer[] roleIds){
        sysRoleService.deleteBatch(roleIds);
        return Response.success();
    }

}
