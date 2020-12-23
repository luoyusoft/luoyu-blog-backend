package com.luoyu.blog.controller.chat;

import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.*;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.chat.Message;
import com.luoyu.blog.entity.chat.User;
import com.luoyu.blog.entity.chat.vo.UserVO;
import com.luoyu.blog.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author luoyu
 * @date 2019-06-11
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private WebsocketServerEndpoint websocketServerEndpoint;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 初始登录接口
     *
     * @return
     */
    @PostMapping("/init")
    public Response init(HttpServletRequest request) throws Exception {
        String ip = IPUtils.getIpAddr(request);
        String borderName = UserAgentUtils.getBorderName(request);
        String browserVersion = UserAgentUtils.getBrowserVersion(request);
        String deviceManufacturer = UserAgentUtils.getDeviceManufacturer(request);
        String devicetype = UserAgentUtils.getDeviceType(request);
        String osVersion = UserAgentUtils.getOsVersion(request);

        String id = EncodeUtils.encoderByMD5(ip + borderName + browserVersion + deviceManufacturer + devicetype + osVersion);

        if (websocketServerEndpoint.isOnline(id)){
            throw new MyException(ResponseEnums.CHAT_USER_REPEAT);
        }

        User oldUserentity = chatService.findById(RedisKeyConstants.CHAT_USER_PREFIX + id);
        if (oldUserentity != null) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(oldUserentity, userVO);
            return Response.success(userVO);
        }

        throw new MyException(ResponseEnums.CHAT_INITT_SUCCESS);
    }

    /**
     * 登录接口
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Response login(HttpServletRequest request, @RequestBody User user) throws Exception {
        if (user == null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getAvatar())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "名称，头像不能为空");
        }

        String ip = IPUtils.getIpAddr(request);
        String borderName = UserAgentUtils.getBorderName(request);
        String browserVersion = UserAgentUtils.getBrowserVersion(request);
        String deviceManufacturer = UserAgentUtils.getDeviceManufacturer(request);
        String devicetype = UserAgentUtils.getDeviceType(request);
        String osVersion = UserAgentUtils.getOsVersion(request);

        String id = EncodeUtils.encoderByMD5(ip + borderName + browserVersion + deviceManufacturer + devicetype + osVersion);

        if (websocketServerEndpoint.isOnline(id)){
            throw new MyException(ResponseEnums.CHAT_USER_REPEAT);
        }

        user.setId(id);
        user.setIp(ip);
        user.setCreateTime(DateUtils.getNowTimeString());
        user.setBorderName(borderName);
        user.setBorderVersion(browserVersion);
        user.setDeviceManufacturer(deviceManufacturer);
        user.setDeviceType(devicetype);
        user.setOsVersion(osVersion);

        User oldUserentity = chatService.findById(id);
        if (oldUserentity != null) {
            String oldName = oldUserentity.getName();
            boolean isChangeName = false;

            if (!oldName.equals(user.getName())) {
                Set<String> names = redisTemplate.opsForSet().members(RedisKeyConstants.CHAT_NAME);
                if (!CollectionUtils.isEmpty(names)) {
                    names.forEach(namesItem -> {
                        if (namesItem.equals(user.getName())) {
                            throw new MyException(ResponseEnums.CHAT_NAME_REPEAT);
                        }
                    });
                }
                isChangeName = true;
            }

            UserVO userVO = new UserVO();
            oldUserentity.setName(user.getName());
            oldUserentity.setAvatar(user.getAvatar());
            BeanUtils.copyProperties(oldUserentity, userVO);

            redisTemplate.boundValueOps(RedisKeyConstants.CHAT_USER_PREFIX + oldUserentity.getId()).set(JsonUtils.objectToJson(oldUserentity));

            if (isChangeName){
                redisTemplate.opsForSet().add(RedisKeyConstants.CHAT_NAME, user.getName());
                redisTemplate.opsForSet().remove(RedisKeyConstants.CHAT_NAME, oldName);
            }

            return Response.success(userVO);
        }

        Set<String> names = redisTemplate.opsForSet().members(RedisKeyConstants.CHAT_NAME);
        if (!CollectionUtils.isEmpty(names)){
            names.forEach(namesItem -> {
                if (namesItem.equals(user.getName())){
                    throw new MyException(ResponseEnums.CHAT_NAME_REPEAT);
                }
            });
        }

        redisTemplate.boundValueOps(RedisKeyConstants.CHAT_USER_PREFIX + user.getId()).set(JsonUtils.objectToJson(user));
        redisTemplate.opsForSet().add(RedisKeyConstants.CHAT_NAME, user.getName());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Response.success(userVO);
    }

    /**
     * 修改接口
     *
     * @param user
     * @return
     */
    @PutMapping("/change")
    public Response change(HttpServletRequest request, @RequestBody User user) throws Exception {
        if (user == null || StringUtils.isEmpty(user.getId())
                || (StringUtils.isEmpty(user.getName()) && StringUtils.isEmpty(user.getAvatar()))){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，名称，头像不能为空");
        }

        String ip = IPUtils.getIpAddr(request);
        String borderName = UserAgentUtils.getBorderName(request);
        String browserVersion = UserAgentUtils.getBrowserVersion(request);
        String deviceManufacturer = UserAgentUtils.getDeviceManufacturer(request);
        String devicetype = UserAgentUtils.getDeviceType(request);
        String osVersion = UserAgentUtils.getOsVersion(request);

        String id = EncodeUtils.encoderByMD5(ip + borderName + browserVersion + deviceManufacturer + devicetype + osVersion);

        if (!id.equals(user.getId())){
            throw new MyException(ResponseEnums.CHAT_NO_AUTH);
        }

        if (!websocketServerEndpoint.isOnline(id)){
            throw new MyException(ResponseEnums.CHAT_USER_OFF_LINE);
        }

        user.setId(id);
        user.setIp(ip);
        user.setCreateTime(DateUtils.getNowTimeString());
        user.setBorderName(borderName);
        user.setBorderVersion(browserVersion);
        user.setDeviceManufacturer(deviceManufacturer);
        user.setDeviceType(devicetype);
        user.setOsVersion(osVersion);

        User oldUserentity = chatService.findById(id);
        if (oldUserentity != null) {
            String oldName = oldUserentity.getName();
            boolean isChangeName = false;

            if (!oldName.equals(user.getName())){
                Set<String> names = redisTemplate.opsForSet().members(RedisKeyConstants.CHAT_NAME);
                if (!CollectionUtils.isEmpty(names)){
                    names.forEach(namesItem -> {
                        if (namesItem.equals(user.getName())){
                            throw new MyException(ResponseEnums.CHAT_NAME_REPEAT);
                        }
                    });
                }
                isChangeName = true;
            }

            UserVO userVO = new UserVO();
            oldUserentity.setName(user.getName());
            oldUserentity.setAvatar(user.getAvatar());
            BeanUtils.copyProperties(oldUserentity, userVO);

            redisTemplate.boundValueOps(RedisKeyConstants.CHAT_USER_PREFIX + oldUserentity.getId()).set(JsonUtils.objectToJson(oldUserentity));
            if (isChangeName){
                redisTemplate.opsForSet().add(RedisKeyConstants.CHAT_NAME, user.getName());
                redisTemplate.opsForSet().remove(RedisKeyConstants.CHAT_NAME, oldName);
            }

            return Response.success(userVO);
        }

        throw new MyException(ResponseEnums.CHAT_USER_NOT_EXIST);
    }

    /**
     * 获取当前窗口用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response info(@PathVariable("id") String id) {
        User user = chatService.findById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Response.success(userVO);
    }

    /**
     * 向指定窗口推送消息
     *
     * @param toId    接收方ID
     * @param message 消息
     * @return
     */
    @PostMapping("/push/{toId}")
    public Response push(@PathVariable("toId") String toId, @RequestBody Message message) {
        WebsocketServerEndpoint endpoint = new WebsocketServerEndpoint();
        endpoint.sendTo(toId, message);
        return Response.success();
    }

    /**
     * 获取在线用户列表
     *
     * @return
     */
    @GetMapping("/online/list")
    public Response onlineList() {
        return Response.success(chatService.onlineList());
    }

    /**
     * 获取公共聊天消息内容
     *
     * @return
     */
    @GetMapping("/common")
    public Response commonList() {
        return Response.success(chatService.commonList());
    }

    /**
     * 获取指定用户的聊天消息内容
     *
     * @param fromId 该用户ID
     * @param toId   哪个窗口
     * @return
     */
    @GetMapping("/self/{fromId}/{toId}")
    public Response selfList(@PathVariable("fromId") String fromId, @PathVariable("toId") String toId) {
        List<Message> list = chatService.selfList(fromId, toId);
        return Response.success(list);
    }

    /**
     * 退出登录
     *
     * @param id 用户ID
     * @return
     */
    @DeleteMapping("/{id}")
    public Response logout(@PathVariable("id") String id) {
        chatService.delete(id);
        return Response.success();
    }

}
