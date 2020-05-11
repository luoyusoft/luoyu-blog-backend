package com.jinhaoxun.blog.project.tt.service.shiroservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.accommon.constant.AbstractConstant;
import com.jinhaoxun.blog.common.exception.ExceptionFactory;
import com.jinhaoxun.blog.project.tt.pojo.shiro.User;
import com.jinhaoxun.accommon.pojo.shiro.request.*;
import com.jinhaoxun.blog.common.response.ResponseFactory;
import com.jinhaoxun.blog.common.response.ResponseMsg;
import com.jinhaoxun.blog.common.response.ResponseResult;
import com.jinhaoxun.blog.common.util.encodeutil.BcryptUtil;
import com.jinhaoxun.blog.common.util.idutil.IdUtil;
import com.jinhaoxun.blog.common.util.otherutil.JwtUtil;
import com.jinhaoxun.blog.project.tt.dao.shiromapper.UserMapper;
import com.jinhaoxun.blog.framework.redis.jedisutil.JedisUtil;
import com.jinhaoxun.blog.framework.redis.redisutil.RedisUtil;
import com.jinhaoxun.blog.project.tt.service.applyservice.IMessageService;
import com.jinhaoxun.blog.project.tt.service.applyservice.INotifyService;
import com.jinhaoxun.blog.project.tt.service.shiroservice.IUserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 用户服务类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IMessageService iMessageService;
    @Resource
    private INotifyService notifyService;
    @Resource
    private ExceptionFactory exceptionFactory;
    @Resource
    private RedisUtil redisUtil;

    /**
     * @author jinhaoxun
     * @description 进行登录
     * @param userLoginReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    @Override
    public ResponseResult addSession(UserLoginReq userLoginReq, HttpServletResponse response) throws Exception{
        Long userId;
        if(userLoginReq.getType().equals(AbstractConstant.USER_REGISTER_TYPE_PHONE)){
            String phone = userLoginReq.getPhone();
            userId = userMapper.selectIdByPhone(phone);
        }else{
            String email = userLoginReq.getEmail();
            userId = userMapper.selectIdByEmail(email);
        }
        String password = userLoginReq.getPassword();
/*        Assert.notNull(userName, "用户名不能为空！");
        Assert.notNull(password, "密码不能为空！");*/
        if(userId == null){
            throw exceptionFactory.build(ResponseMsg.USER_NOT_EXIST.getCode(),"账号或者密码错误");
        }
        String realPassword = userMapper.selectPassword(userId);
        if (!BcryptUtil.checkPasswordByBcrypt(password, realPassword)) {
            throw exceptionFactory.build(ResponseMsg.PASSWORD_WRONG.getCode(),"账号或者密码错误");
        }
        Boolean ban = userMapper.selectBan(userId);
        Boolean status = userMapper.selectStatus(userId);
        //账号是否被封
        if (ban) {
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_IS_BLOCKED.getCode(),ResponseMsg.ACCOUNT_IS_BLOCKED.getMsg());
        }
        //账号是否注销
        if (!status) {
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_IS_CANCELLED.getCode(),ResponseMsg.ACCOUNT_IS_CANCELLED.getMsg());
        }
        //验证成功后处理
        this.loginSuccess(userId, realPassword, response);
        //获取用户推送消息
/*        ResponseResult<List<Message>> responseResult = iMessageService.getSystemMessageByUserId(userId);
        if(responseResult.getData().size() > 0 ){
            ListIterator listIterator = responseResult.getData().listIterator();
            while(listIterator.hasNext()){
                iMessageService.sendSystemMessage((SystemMessage)listIterator.next());
            }
        }*/
        return ResponseFactory.buildSuccessResponse("登录成功!");
    }

    /**
     * @author jinhaoxun
     * @description 登录后更新缓存，生成token，设置响应头部信息
     * @param userId 用户id
     * @param password 用户密码
     * @param response 请求响应体
     */
    private void loginSuccess(Long userId, String password, HttpServletResponse response) {
        // 清除可能存在的Shiro权限信息缓存
        String tokenKey= AbstractConstant.SHIRO_ROLE_PERMISSION_KEY + userId;
        if (JedisUtil.exists(tokenKey)) {
            JedisUtil.delKey(tokenKey);
        }
        //获取当前时间戳
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());

        JSONObject json = new JSONObject();
        //生成token
        String token = JwtUtil.createToken(Long.toString(userId), password , currentTimeMillis);
        json.put("token",token );
        //更新RefreshToken缓存的时间戳
        String refreshToken = AbstractConstant.REFRESH_TOKEN + userId;
        redisUtil.getSet(refreshToken, currentTimeMillis, AbstractConstant.REFRESH_TOKEN_CHECK_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        //写入header
        response.setHeader(AbstractConstant.REQUEST_AUTH_HEADER, token);
        response.setHeader("Access-Control-Expose-Headers", AbstractConstant.REQUEST_AUTH_HEADER);
    }

    /**
     * @author jinhaoxun
     * @description 退出登录
     * @param userId 用户id
     * @return Boolean 是否退出成功
     */
    @Override
    public void deleteSession(Long userId) {
        // 清除可能存在的Shiro权限信息缓存
        String tokenKey= AbstractConstant.SHIRO_ROLE_PERMISSION_KEY + userId;
        if (JedisUtil.exists(tokenKey)) {
            JedisUtil.delKey(tokenKey);
        }
        log.info(String.format("用户 {} 退出成功", userId));
    }

    /**
     * @author jinhaoxun
     * @description 用户注册
     * @param userRegisterReq 注册信息参数
     * @return ResponseResult 是否注册成功
     * @throws Exception
     */
    @Override
    public ResponseResult addUser(UserRegisterReq userRegisterReq) throws Exception {
        User user = new User();
        if(userRegisterReq.getType().equals(AbstractConstant.USER_REGISTER_TYPE_PHONE)){
            user.setPhone(userRegisterReq.getPhone());
        }else{
            user.setEmail(userRegisterReq.getEmail());
        }
        Long userId = IdUtil.getId();
        user.setUserId(userId);
        String password = BcryptUtil.encoderByBcrypt(userRegisterReq.getPassword());
        user.setPassword(password);
        user.setName(userRegisterReq.getName());
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdaterId(userId);
        user.setUpdateTime(now);
        user.setBan(false);
        user.setStatus(true);
        user.setArticleComment(0);
        user.setArticlePraise(0);
        user.setArticle(0);
        int count = userMapper.insert(user);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_REGISTRATION_FAIL.getCode(),ResponseMsg.ACCOUNT_REGISTRATION_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("注册成功");
    }

    /**
     * @author jinhaoxun
     * @description 用户注销
     * @param userIdReq 用户id
     * @return ResponseResult 是否注销成功
     * @throws Exception
     */
    @Override
    public ResponseResult deleteUser(UserIdReq userIdReq) throws Exception {
        int count = userMapper.updateStatus(userIdReq.getUserId());
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_CANAEL_FAIL.getCode(),ResponseMsg.ACCOUNT_CANAEL_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("注销成功");
    }

    /**
     * @author jinhaoxun
     * @description 获取验证码
     * @param getCodeReq 获取验证码类型参数
     * @return ResponseResult 获取到的验证码
     * @throws Exception
     */
    @Override
    public ResponseResult getCode(GetCodeReq getCodeReq) throws Exception {
        if(getCodeReq.getType().equals(AbstractConstant.USER_REGISTER_TYPE_PHONE)){
            String userLogInCodeKey = AbstractConstant.USER_LOG_IN_CODE + getCodeReq.getPhone();
            if (JedisUtil.exists(userLogInCodeKey)) {
                exceptionFactory.build(ResponseMsg.REPEAT_GET_USER_LOG_IN_CODE.getCode(),ResponseMsg.REPEAT_GET_USER_LOG_IN_CODE.getMsg());
            }
            ResponseResult responseResult = notifyService.getPhoneCode(getCodeReq.getPhone());
            redisUtil.put(AbstractConstant.USER_LOG_IN_CODE + responseResult.getData().toString(),responseResult.getData().toString(),
                    AbstractConstant.USER_LOG_IN_CODE_EXPIRATION_TIME,TimeUnit.SECONDS);
            return ResponseFactory.buildSuccessResponse("获取验证码成功！");
        }else{
            String userLogInCodeKey = AbstractConstant.USER_LOG_IN_CODE + getCodeReq.getEmail();
            if (JedisUtil.exists(userLogInCodeKey)) {
                exceptionFactory.build(ResponseMsg.REPEAT_GET_USER_LOG_IN_CODE.getCode(),ResponseMsg.REPEAT_GET_USER_LOG_IN_CODE.getMsg());
            }
            ResponseResult responseResult = notifyService.getEmailCode(getCodeReq.getEmail());
            redisUtil.put(AbstractConstant.USER_LOG_IN_CODE + responseResult.getData().toString(),responseResult.getData().toString(),
                    AbstractConstant.USER_LOG_IN_CODE_EXPIRATION_TIME,TimeUnit.SECONDS);
            return ResponseFactory.buildSuccessResponse("获取验证码成功！");
        }
    }

    /**
     * @author jinhaoxun
     * @description 验证码登录
     * @param getCodeReq 登录信息参数
     * @param response 请求响应体
     * @return ResponseResult 登录操作结果
     * @throws Exception
     */
    @Override
    public ResponseResult addCodeSession(GetCodeReq getCodeReq, HttpServletResponse response) throws Exception {
        String userLogInCodeKey = AbstractConstant.USER_LOG_IN_CODE + getCodeReq.getEmail();
        if (!JedisUtil.exists(userLogInCodeKey)) {
            exceptionFactory.build(ResponseMsg.USER_LOG_IN_CODE_EXPIRATIONED.getCode(),ResponseMsg.USER_LOG_IN_CODE_EXPIRATIONED.getMsg());
        }
        String code = redisUtil.get(userLogInCodeKey);
        if(!code.equals(getCodeReq.getCode())){
            exceptionFactory.build(ResponseMsg.USER_LOG_IN_CODE_WRONG.getCode(),ResponseMsg.USER_LOG_IN_CODE_WRONG.getMsg());
        }
        Long userId;
        if(getCodeReq.getType().equals(AbstractConstant.USER_REGISTER_TYPE_PHONE)){
            userId = userMapper.selectIdByPhone(getCodeReq.getPhone());
        }else{
            userId = userMapper.selectIdByEmail(getCodeReq.getEmail());
        }
        Boolean ban = userMapper.selectBan(userId);
        Boolean status = userMapper.selectStatus(userId);
        //账号是否被封
        if (ban) {
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_IS_BLOCKED.getCode(),ResponseMsg.ACCOUNT_IS_BLOCKED.getMsg());
        }
        //账号是否注销
        if (!status) {
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_IS_CANCELLED.getCode(),ResponseMsg.ACCOUNT_IS_CANCELLED.getMsg());
        }
        String password = userMapper.selectPassword(userId);
        //验证成功后处理
        this.loginSuccess(userId, password, response);
        JedisUtil.delKey(userLogInCodeKey);
        //获取用户推送消息
/*        ResponseResult<List<Message>> responseResult = iMessageService.getSystemMessageByUserId(userId);
        if(responseResult.getData().size() > 0 ){
            return ResponseFactory.buildSuccessResponse(responseResult.getData(),"登录成功!");
        }*/
        return ResponseFactory.buildSuccessResponse("登录成功!");
    }

    /**
     * @author jinhaoxun
     * @description 修改密码
     * @param updatePasswordReq 修改密码信息参数
     * @return ResponseResult 修改密码操作结果
     * @throws Exception
     */
    @Override
    public ResponseResult updatePassword(UpdatePasswordReq updatePasswordReq) throws Exception {
        String password = BcryptUtil.encoderByBcrypt(updatePasswordReq.getPassword());
        int count = userMapper.updatePassword(updatePasswordReq.getUserId(),password);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.PASSWORD_CHANGE_FAIL.getCode(),ResponseMsg.PASSWORD_CHANGE_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("修改成功");
    }

    /**
     * @author jinhaoxun
     * @description 账号封禁
     * @param userIdReq 用户id
     * @return ResponseResult 是否封禁成功
     * @throws Exception
     */
    @Override
    public ResponseResult addBan(UserIdReq userIdReq) throws Exception {
        int count = userMapper.updateBan(userIdReq.getUserId(),true);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_BLOCK_FAIL.getCode(),ResponseMsg.ACCOUNT_BLOCK_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("封禁成功");
    }

    /**
     * @author jinhaoxun
     * @description 账号解封
     * @param userIdReq 用户id
     * @return ResponseResult 是否解封成功
     * @throws Exception
     */
    @Override
    public ResponseResult deleteBan(UserIdReq userIdReq) throws Exception {
        int count = userMapper.updateBan(userIdReq.getUserId(),false);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ACCOUNT_UNSEALING_FAIL.getCode(),ResponseMsg.ACCOUNT_UNSEALING_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("解封成功");
    }

    /**
     * @author jinhaoxun
     * @description 获取用户信息
     * @param userIdReq 用户id
     * @return ResponseResult 获取到的用户信息
     * @throws Exception
     */
    @Override
    public ResponseResult getUser(UserIdReq userIdReq) throws Exception {
        User user = userMapper.selectById(userIdReq.getUserId());
        if(user == null){
            throw exceptionFactory.build(ResponseMsg.GET_USERINFO_FAIL.getCode(),ResponseMsg.GET_USERINFO_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse(user);
    }

    /**
     * @author jinhaoxun
     * @description 更新用户信息
     * @param user 用户信息
     * @return ResponseResult 是否更新成功
     * @throws Exception
     */
    @Override
    public ResponseResult updateUser(User user) throws Exception {
        int count = userMapper.updateById(user);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.UPDATE_USERINFO_FAIL.getCode(),ResponseMsg.UPDATE_USERINFO_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("更新成功");
    }

    /**
     * @author jinhaoxun
     * @description 获取所有账号列表
     * @return ResponseResult 获取到的账号列表
     */
    @Override
    public ResponseResult getUserList() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.orderByDesc(AbstractConstant.USER_CREATE_TIME);
        List<User> userList = userMapper.selectList(qw);
        return ResponseFactory.buildSuccessResponse(userList);
    }
}
