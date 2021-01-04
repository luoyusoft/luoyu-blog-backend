package com.luoyu.blog.controller.chat;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.util.EncodeUtils;
import com.luoyu.blog.common.util.IPUtils;
import com.luoyu.blog.common.util.UserAgentUtils;
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

    @Autowired
    private WebsocketServerEndpoint websocketServerEndpoint;

    /**
     * 初始化接口
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

        return Response.success(chatService.init(id));
    }

    /**
     * 登录接口
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    @LogView(module = 2)
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

        return Response.success(chatService.login(user));
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

        return Response.success(chatService.change(user));
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

}
