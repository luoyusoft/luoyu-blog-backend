package com.luoyu.blog.controller.chat;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.chat.Message;
import com.luoyu.blog.entity.chat.User;
import com.luoyu.blog.entity.chat.vo.UserVO;
import com.luoyu.blog.service.chat.ChatService;
import com.luoyu.blog.service.chat.WebsocketServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 初始化接口
     *
     * @return
     */
    @PostMapping("/init")
    public Response init(HttpServletRequest request) throws Exception {
        return Response.success(chatService.init(request));
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

        return Response.success(chatService.login(request, user));
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

        return Response.success(chatService.change(request, user));
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
