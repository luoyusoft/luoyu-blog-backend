package com.jinhx.blog.controller.sys;

import com.jinhx.blog.common.constants.SysConstants;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.entity.base.AbstractController;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.sys.SysUser;
import com.jinhx.blog.entity.sys.vo.PasswordVO;
import com.jinhx.blog.service.sys.SysUserRoleService;
import com.jinhx.blog.service.sys.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-10-08
 */
@RestController
public class SysUserController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/manage/sys/user/info")
    public Response info(){
        return Response.success(getUser());
    }

    /**
     * 所有用户列表
     */
    @GetMapping("/manage/sys/user/list")
    @RequiresPermissions("sys:user:list")
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("username") String username){
        Integer createUserId = null;
        //只有超级管理员，才能查看所有管理员列表
        if(SysConstants.SUPER_ADMIN.equals(getUserId())){
            createUserId = getUserId();
        }
        PageUtils userPage = sysUserService.queryPage(page, limit, username, createUserId);

        return Response.success(userPage);
    }

    /**
     * 修改密码
     * @param passwordVO
     * @return
     */
    @PutMapping("/manage/sys/user/password")
    public Response password(@RequestBody PasswordVO passwordVO){
        if(StringUtils.isEmpty(passwordVO.getNewPassword())) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "新密码不能为空");
        }
        //sha256加密
        String password = new Sha256Hash(passwordVO.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordVO.getNewPassword(), getUser().getSalt()).toHex();

        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "原密码不正确");
        }

        return Response.success();
    }

    /**
     * 保存用户
     */
    @PostMapping("/manage/sys/user/save")
    @RequiresPermissions("sys:user:save")
    public Response save(@RequestBody SysUser user){
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.save(user);

        return Response.success();
    }

    /**
     * 修改用户
     */
    @PutMapping("/manage/sys/user/update")
    @RequiresPermissions("sys:user:update")
    public Response update(@RequestBody SysUser user){
        user.setCreateUserId(getUserId());
        sysUserService.updateById(user);

        return Response.success();
    }

    /**
     * 删除用户
     */
    @PostMapping("/manage/sys/user/delete")
    @RequiresPermissions("sys:user:delete")
    public Response delete(@RequestBody Integer[] userIds){
        if (userIds == null || userIds.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "userIds不能为空");
        }

        if (userIds.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "userIds不能超过100个");
        }

        if(ArrayUtils.contains(userIds, SysConstants.SUPER_ADMIN)){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "系统管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return Response.success();
    }

    /**
     * 用户信息
     */
    @GetMapping("/manage/sys/user/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public Response info(@PathVariable("userId") Integer userId){
        SysUser user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Integer> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return Response.success(user);
    }

}
