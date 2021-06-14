package com.jinhx.blog.controller.sys;

import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.common.util.SysAdminUtils;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.sys.SysUser;
import com.jinhx.blog.entity.sys.dto.SysUserDTO;
import com.jinhx.blog.entity.sys.vo.PasswordVO;
import com.jinhx.blog.service.sys.SysUserRoleService;
import com.jinhx.blog.service.sys.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SysUserController
 * @author jinhx
 * @since 2018-10-08
 */
@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/manage/sys/user/info")
    public Response info(){
        return Response.success(SysAdminUtils.getUserDTO());
    }

    /**
     * 分页查询用户信息列表
     * @param page 页码
     * @param limit 页数
     * @param username 用户名
     * @return 用户信息列表
     */
    @GetMapping("/manage/sys/user/list")
    @RequiresPermissions("sys:user:list")
    public Response listSysUsers(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("username") String username){
        PageUtils userPage = sysUserService.queryPage(page, limit, username);

        // 如果不是超级管理员，则不展示超级管理员
        if(!SysAdminUtils.isSuperAdmin()){
            userPage.setList(userPage.getList().stream().filter(item -> !((SysUser)item).getId().equals(SysAdminUtils.sysSuperAdminRoleId)).collect(Collectors.toList()));
        }

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
        String password = new Sha256Hash(passwordVO.getPassword(), SysAdminUtils.getUserDTO().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordVO.getNewPassword(), SysAdminUtils.getUserDTO().getSalt()).toHex();

        boolean flag = sysUserService.updatePassword(SysAdminUtils.getUserId(), password, newPassword);
        if(!flag){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "原密码不正确");
        }

        return Response.success();
    }

    /**
     * 新增用户信息
     * @param sysUserDTO 用户信息
     * @return 新增结果
     */
    @PostMapping("/manage/sys/user/save")
    @RequiresPermissions("sys:user:save")
    public Response insertSysUser(@RequestBody SysUserDTO sysUserDTO){
        ValidatorUtils.validateEntity(sysUserDTO, AddGroup.class);

        sysUserDTO.setCreaterId(SysAdminUtils.getUserId());
        sysUserDTO.setUpdaterId(SysAdminUtils.getUserId());
        if(StringUtils.isEmpty(sysUserDTO.getProfile())){
            sysUserDTO.setProfile(SysUser.sysUserDefaultProfile);
        }
        sysUserService.insertSysUser(sysUserDTO);

        return Response.success();
    }

    /**
     * 根据用户id更新用户信息
     * @param sysUserDTO 用户信息
     * @return 更新结果
     */
    @PutMapping("/manage/sys/user/update")
    @RequiresPermissions("sys:user:update")
    public Response updateSysUserById(@RequestBody SysUserDTO sysUserDTO){
        sysUserDTO.setCreaterId(SysAdminUtils.getUserId());
        sysUserDTO.setUpdaterId(SysAdminUtils.getUserId());
        sysUserService.updateSysUserById(sysUserDTO);

        return Response.success();
    }

    /**
     * 根据用户id列表批量删除用户
     * @param userIds 用户id列表
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

        if(ArrayUtils.contains(userIds, SysAdminUtils.getUserId())){
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
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, sysUserDTO);
        // 获取用户所属的角色列表
        List<Integer> roleIdList = sysUserRoleService.getRoleIdListByUserId(userId);
        sysUserDTO.setRoleIdList(roleIdList);

        return Response.success(sysUserDTO);
    }

}
