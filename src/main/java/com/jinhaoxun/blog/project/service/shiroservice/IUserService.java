package com.jinhaoxun.blog.project.tt.service.shiroservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhaoxun.blog.project.tt.pojo.shiro.User;
import com.jinhaoxun.accommon.pojo.shiro.request.*;
import com.jinhaoxun.blog.common.response.ResponseResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户服务接口
 */
public interface IUserService extends IService<User> {

    /**
     * @author jinhaoxun
     * @description 进行登录
     * @param userLoginReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    ResponseResult addSession(UserLoginReq userLoginReq, HttpServletResponse response) throws Exception;

    /**
     * @author jinhaoxun
     * @description 退出登录
     * @param userId 用户id
     * @return Boolean 是否退出成功
     */
    void deleteSession(Long userId) throws Exception;

    /**
     * @author jinhaoxun
     * @description 用户注册
     * @param userRegisterReq 注册信息参数
     * @return ResponseResult 是否注册成功
     * @throws Exception
     */
    ResponseResult addUser(UserRegisterReq userRegisterReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 用户注销
     * @param userIdReq 用户id
     * @return ResponseResult 是否注销成功
     * @throws Exception
     */
    ResponseResult deleteUser(UserIdReq userIdReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 获取验证码
     * @param getCodeReq 获取验证码类型参数
     * @return ResponseResult 获取到的验证码
     * @throws Exception
     */
    ResponseResult getCode(GetCodeReq getCodeReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 验证码登录
     * @param getCodeReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    ResponseResult addCodeSession(GetCodeReq getCodeReq, HttpServletResponse response) throws Exception;

    /**
     * @author jinhaoxun
     * @description 修改密码
     * @param updatePasswordReq 修改密码信息参数
     * @return ResponseResult 修改密码操作结果
     * @throws Exception
     */
    ResponseResult updatePassword(UpdatePasswordReq updatePasswordReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 账号封禁
     * @param userIdReq 用户id
     * @return ResponseResult 是否封禁成功
     * @throws Exception
     */
    ResponseResult addBan(UserIdReq userIdReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 账号解封
     * @param userIdReq 用户id
     * @return ResponseResult 是否解封成功
     * @throws Exception
     */
    ResponseResult deleteBan(UserIdReq userIdReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 获取用户信息
     * @param userIdReq 用户id
     * @return ResponseResult 获取到的用户信息
     * @throws Exception
     */
    ResponseResult getUser(UserIdReq userIdReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 更新用户信息
     * @param user 用户信息
     * @return ResponseResult 是否更新成功
     * @throws Exception
     */
    ResponseResult updateUser(User user) throws Exception;

    /**
     * @author jinhaoxun
     * @description 获取所有账号列表
     * @return ResponseResult 获取到的账号列表
     */
    ResponseResult getUserList() throws Exception;
}
