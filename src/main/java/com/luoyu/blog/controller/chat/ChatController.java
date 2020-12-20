package com.luoyu.blog.controller.chat;

import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.chat.Message;
import com.luoyu.blog.entity.chat.User;
import com.luoyu.blog.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 登录接口
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        user.setId(Instant.now().toEpochMilli());
        Set<String> keys = redisTemplate.keys(RedisKeyConstants.CHAT_USER_PREFIX + RedisKeyConstants.REDIS_MATCH_PREFIX);
        if (keys != null && keys.size() > 0) {
            keys.forEach(key -> {
                User entity = chatService.findById(key);
                if (entity != null) {
                    if ((entity.getName()).equals(user.getName())) {
                        throw new MyException(ResponseEnums.CHAT_USER_REPEAT);
                    }
                }
            });
        }
        redisTemplate.boundValueOps(RedisKeyConstants.CHAT_USER_PREFIX + user.getId()).set(JsonUtils.objectToJson(user));
        return Response.success(user.getId());
    }

    /**
     * 获取当前窗口用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response info(@PathVariable("id") String id) {
        return Response.success(chatService.findById(id));
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
