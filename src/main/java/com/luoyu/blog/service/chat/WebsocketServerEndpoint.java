package com.luoyu.blog.service.chat;

import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.entity.chat.Message;
import com.luoyu.blog.entity.chat.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author luoyu
 * @date 2019-06-10
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chat/{id}")
public class WebsocketServerEndpoint {

    private static ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService) {
        WebsocketServerEndpoint.chatService = chatService;
    }

    //在线连接数
    private static long online = 0;

    //用于存放当前Websocket对象的Set集合
    private static CopyOnWriteArraySet<WebsocketServerEndpoint> websocketServerEndpoints = new CopyOnWriteArraySet<>();

    //与客户端的会话Session
    private Session session;

    //当前会话窗口ID
    private String fromId = "";

    /**
     * 链接成功调用的方法
     *
     * @param session
     * @param id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("Websocket链接成功：{}", id);
        this.session = session;

        //将当前websocket对象存入到Set集合中
        websocketServerEndpoints.add(this);

        //在线人数+1
        addOnlineCount();

        log.info("Websocket有新窗口开始监听：" + id + "，当前在线人数为：" + getOnlineCount());

        fromId = id;

        try {
            User user = chatService.findById(fromId);
            //群发消息
            Map<String, Object> map = new HashMap<>();
            map.put("msg", "用户\"" + user.getName() + "\"已上线");
            sendMore(JsonUtils.objectToJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("Websocket链接成功关闭");

        //移除当前Websocket对象
        websocketServerEndpoints.remove(this);

        //在内线人数-1
        subOnLineCount();

        log.info("Websocket链接关闭，当前在线人数：" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("Websocket接收到窗口：" + fromId + "的信息：" + message);

        chatService.pushMessage(fromId, null, message);

        //群发消息
        sendMore(getData(null, message));
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    /**
     * 推送消息
     *
     * @param message
     */
    private void sendMessage(String message) throws Exception {
        session.getBasicRemote().sendText(message);
    }

    /**
     * 封装返回消息
     *
     * @param toId    指定窗口ID
     * @param message 消息内容
     * @return
     * @throws Exception
     */
    private String getData(String toId, String message) {
        Message entity = new Message();
        entity.setMessage(message);
        entity.setCreateTime(DateUtils.getNowTimeString());
        entity.setFrom(chatService.findById(fromId));
        entity.setTo(chatService.findById(toId));
        return JsonUtils.objectToJson(Response.success(entity));
    }

    /**
     * 群发消息
     *
     * @param data
     */
    private void sendMore(String data) {
        for (WebsocketServerEndpoint websocketServerEndpoint : websocketServerEndpoints) {
            try {
                websocketServerEndpoint.sendMessage(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定窗口推送消息
     *
     * @param entity 推送消息
     * @param toId   接收方ID
     */
    public void sendTo(String toId, Message entity) {
        fromId = entity.getFrom().getId();
        if (websocketServerEndpoints.size() <= 1) {
            throw new MyException(ResponseEnums.CHAT_USER_OFF_LINE);
        }
        boolean flag = false;
        for (WebsocketServerEndpoint endpoint : websocketServerEndpoints) {
            try {
                if (endpoint.fromId.equals(toId)) {
                    flag = true;
                    log.info("Websocket：" + entity.getFrom().getId() + "推送消息到窗口：" + toId + " ，推送内容：" + entity.getMessage());

                    endpoint.sendMessage(getData(toId, entity.getMessage()));
                    chatService.pushMessage(fromId, toId, entity.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResponseEnums.CHAT_SEND_ERROR);
            }
        }
        if (!flag) {
            throw new MyException(ResponseEnums.CHAT_USER_OFF_LINE);
        }
    }

    /**
     * 是否在线
     */
    public Boolean isOnline(String id) {
        if (websocketServerEndpoints.size() < 1) {
            return false;
        }
        for (WebsocketServerEndpoint endpoint : websocketServerEndpoints) {
            if (endpoint.fromId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void subOnLineCount() {
        WebsocketServerEndpoint.online--;
    }

    private synchronized long getOnlineCount() {
        return online;
    }

    private void addOnlineCount() {
        WebsocketServerEndpoint.online++;
    }

}
