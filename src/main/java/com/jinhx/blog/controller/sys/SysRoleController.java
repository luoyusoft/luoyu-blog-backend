package com.jinhx.blog.controller.sys;

import com.jinhx.blog.common.constants.SysConstants;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.base.AbstractController;
import com.jinhx.blog.entity.sys.SysRole;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.service.sys.SysRoleMenuService;
import com.jinhx.blog.service.sys.SysRoleService;
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
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 分页查询角色列表
     * @return
     */
    @GetMapping("/manage/sys/role/list")
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
    @GetMapping("/manage/sys/role/select")
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
    @PostMapping("/manage/sys/role/save")
    @RequiresPermissions("sys:role:save")
    public Response save(@RequestBody SysRole role){
        ValidatorUtils.validateEntity(role, AddGroup.class);
        role.setCreateUserId(getUserId());
        sysRoleService.save(role);

        return Response.success();
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @PutMapping("/manage/sys/role/update")
    @RequiresPermissions("sys:role:update")
    public Response update(@RequestBody SysRole role){
        ValidatorUtils.validateEntity(role, AddGroup.class);
        role.setCreateUserId(getUserId());
        sysRoleService.updateById(role);

        return Response.success();
    }

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    @GetMapping("/manage/sys/role/info/{roleId}")
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
    @DeleteMapping("/manage/sys/role/delete")
    @RequiresPermissions("sys:role:delete")
    public Response delete(@RequestBody Integer[] roleIds){
        if (roleIds == null || roleIds.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "roleIds不能为空");
        }

        if (roleIds.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "roleIds不能超过100个");
        }

        sysRoleService.deleteBatch(roleIds);
        return Response.success();
    }

}
