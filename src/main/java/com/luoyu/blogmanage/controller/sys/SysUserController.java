package com.luoyu.blogmanage.controller.sys;

import com.luoyu.blogmanage.common.constants.SysConstants;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.common.validator.group.AddGroup;
import com.luoyu.blogmanage.common.validator.group.UpdateGroup;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.base.Result;
import com.luoyu.blogmanage.entity.sys.SysUser;
import com.luoyu.blogmanage.entity.sys.form.PasswordForm;
import com.luoyu.blogmanage.service.sys.SysUserRoleService;
import com.luoyu.blogmanage.service.sys.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-10-08
 */
@RestController
@RequestMapping("/admin/sys/user")
public class SysUserController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public Result info(){
        return Result.ok().put("user", getUser());
    }

    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public Result list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        if(SysConstants.SUPER_ADMIN.equals(getUserId())){
            params.put("createUserId", getUserId());
        }
        PageUtils page = sysUserService.queryPage(params);

        return Result.ok().put("page", page);
    }

    /**
     * 修改密码
     * @param passwordForm
     * @return
     */
    @PutMapping("/password")
    public Result password(@RequestBody PasswordForm passwordForm){
        if(StringUtils.isEmpty(passwordForm.getNewPassword())) {
            return Result.error("新密码不能为空");
        }
        //sha256加密
        String password = new Sha256Hash(passwordForm.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordForm.getNewPassword(), getUser().getSalt()).toHex();

        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            return Result.error("原密码不正确");
        }

        return Result.ok();
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public Result save(@RequestBody SysUser user){
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.save(user);

        return Result.ok();
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody SysUser user){
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.updateById(user);

        return Result.ok();
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Integer[] userIds){
        if(ArrayUtils.contains(userIds, SysConstants.SUPER_ADMIN)){
            return Result.error("系统管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return Result.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return Result.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public Result info(@PathVariable("userId") Integer userId){
        SysUser user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Integer> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return Result.ok().put("user", user);
    }

}
