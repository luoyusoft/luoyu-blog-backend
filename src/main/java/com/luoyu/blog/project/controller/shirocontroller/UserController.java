package com.luoyu.blog.project.controller.shirocontroller;

import com.luoyu.blog.project.pojo.shiro.User;
import com.luoyu.blog.project.service.shiroservice.IUserService;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.project.pojo.shiro.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户前端控制器
 */
@RequestMapping("/user")
@RestController
@Api("用户接口")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * @author jinhaoxun
     * @description 进行登录
     * @param userLoginReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    @PostMapping(value="/session", produces = "application/json; charset=UTF-8")
    @ApiOperation("进行登录")
    public ResponseResult addSession(@Validated @RequestBody UserLoginReq userLoginReq, HttpServletResponse response) throws Exception {
        return userService.addSession(userLoginReq,response);
    }

    /**
     * @author jinhaoxun
     * @description 用户注册
     * @param userRegisterReq 注册信息参数
     * @return ResponseResult 是否注册成功
     * @throws Exception
     */
    @PostMapping(value="/user", produces = "application/json; charset=UTF-8")
    @ApiOperation("用户注册")
    public ResponseResult addUser(@Validated @RequestBody UserRegisterReq userRegisterReq) throws Exception {
        return userService.addUser(userRegisterReq);
    }

    /**
     * @author jinhaoxun
     * @description 用户注销
     * @param userIdReq 用户id
     * @return ResponseResult 是否注销成功
     * @throws Exception
     */
    @PatchMapping(value="/user", produces = "application/json; charset=UTF-8")
    @ApiOperation("用户注销")
    public ResponseResult deleteUser(@Validated @RequestBody UserIdReq userIdReq) throws Exception {
        return userService.deleteUser(userIdReq);
    }

    /**
     * @author jinhaoxun
     * @description 获取验证码
     * @param getCodeReq 获取验证码类型参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @GetMapping(value="/code", produces = "application/json; charset=UTF-8")
    @ApiOperation("获取验证码")
    public ResponseResult getCode(@Validated GetCodeReq getCodeReq) throws Exception {
        return userService.getCode(getCodeReq);
    }

    /**
     * @author jinhaoxun
     * @description 验证码登录
     * @param getCodeReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    @PostMapping(value="/codesession", produces = "application/json; charset=UTF-8")
    @ApiOperation("验证码登录")
    public ResponseResult addCodeSession(@Validated @RequestBody GetCodeReq getCodeReq, HttpServletResponse response) throws Exception {
        return userService.addCodeSession(getCodeReq,response);
    }

    /**
     * @author jinhaoxun
     * @description 修改密码
     * @param updatePasswordReq 修改密码信息参数
     * @return ResponseResult 修改密码操作结果
     * @throws Exception
     */
    @PatchMapping(value="/password", produces = "application/json; charset=UTF-8")
    @ApiOperation("更新密码")
    public ResponseResult updatePassword(@Validated @RequestBody UpdatePasswordReq updatePasswordReq) throws Exception {
        return userService.updatePassword(updatePasswordReq);
    }

    /**
     * @author jinhaoxun
     * @description 账号封禁
     * @param userIdReq 用户id
     * @return ResponseResult 是否封禁成功
     * @throws Exception
     */
    @PostMapping(value="/ban", produces = "application/json; charset=UTF-8")
    @ApiOperation("账号封禁")
    public ResponseResult addBan(@Validated @RequestBody UserIdReq userIdReq) throws Exception {
        return userService.addBan(userIdReq);
    }

    /**
     * @author jinhaoxun
     * @description 账号解封
     * @param userIdReq 用户id
     * @return ResponseResult 是否解封成功
     * @throws Exception
     */
    @DeleteMapping(value="/ban", produces = "application/json; charset=UTF-8")
    @ApiOperation("账号解封")
    public ResponseResult deleteBan(@Validated @RequestBody UserIdReq userIdReq) throws Exception {
        return userService.deleteBan(userIdReq);
    }

    /**
     * @author jinhaoxun
     * @description 获取用户信息
     * @param userIdReq 用户id
     * @return ResponseResult 获取到的用户信息
     * @throws Exception
     */
    @GetMapping(value="/user", produces = "application/json; charset=UTF-8")
    @ApiOperation("获取用户信息")
    public ResponseResult getUser(@Validated UserIdReq userIdReq) throws Exception {
        return userService.getUser(userIdReq);
    }

    /**
     * @author jinhaoxun
     * @description 更新用户信息
     * @param user 用户信息
     * @return ResponseResult 是否更新成功
     * @throws Exception
     */
    @PutMapping(value="/user", produces = "application/json; charset=UTF-8")
    @ApiOperation("更新用户信息")
    public ResponseResult updateUser(@Validated @RequestBody User user) throws Exception {
        return userService.updateUser(user);
    }

    /**
     * @author jinhaoxun
     * @description 获取所有账号列表
     * @return ResponseResult 获取到的账号列表
     */
    @GetMapping(value="/userlist", produces = "application/json; charset=UTF-8")
    @ApiOperation("获取所有账号列表")
    public ResponseResult getUserList() throws Exception {
        return userService.getUserList();
    }
}

