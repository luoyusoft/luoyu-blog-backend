package com.luoyu.blog.project.service.websocketservice.impl;

import com.luoyu.blog.project.service.websocketservice.IWebSocketService;
import com.luoyu.blog.project.pojo.websocket.ResponseMessage;
import com.luoyu.blog.project.pojo.websocket.all.AllRequestMessage;
import com.luoyu.blog.project.pojo.websocket.chat.ChatRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description WebSocket服务类
 */
@Slf4j
@Service
public class WebSocketServiceImpl implements IWebSocketService {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 使用默认交换机订阅/发布消息，默认由stomp自动创建一个持久化队列，参数说明
     */
    private static String stompUrlQueue = "/queue/";

    /**
     * 通过amq.topic交换机订阅/发布消息，订阅时默认创建一个临时队列，通过routing_key与topic进行绑定
     */
    private static String stompUrlTopic = "/topic";

    /**
     * 和”/queue/queuename”相似，两者的区别是与/queue/queuename的区别在于队列不由stomp自动进行创建，
     * 队列不存在失败,这种情况下无论是发送者还是接收者都不会产生队列。 但如果该队列不存在，接收者会报错。
     */
    private static String stompUrlAmqQueue = "/amq/queue/";

    /**
     * 通过交换机订阅/发布消息，交换机需要手动创建，参数说明
     */
    private static String stompUrlExchange = "/exchange/";

    /**
     * @author jinhaoxun
     * @description 单发消息
     * 注解的方法可以使用下列参数:
     * 使用@Payload方法参数用于获取消息中的payload（即消息的内容）
     * 使用@Header 方法参数用于获取特定的头部
     * 使用@Headers方法参数用于获取所有的头部存放到一个map中
     * java.security.Principal 方法参数用于获取在websocket握手阶段使用的用户信息
     * @param requestMessage 发送消息参数
     * @throws Exception
     */
    @Override
    public void messageHandling(ChatRequestMessage requestMessage) throws Exception {
        String destination = stompUrlQueue + HtmlUtils.htmlEscape(requestMessage.getRecipientId());
        ResponseMessage response = new ResponseMessage();
        response.setContent(requestMessage.getContent());
        response.setType(requestMessage.getType());
        response.setSendTime(requestMessage.getSendTime());
        response.setSenderId(requestMessage.getSenderId());
        messagingTemplate.convertAndSend(destination, response);
    }

    /**
     * @author jinhaoxun
     * @description 群发消息
     * 注解的方法可以使用下列参数:
     * 使用@Payload方法参数用于获取消息中的payload（即消息的内容）
     * 使用@Header 方法参数用于获取特定的头部
     * 使用@Headers方法参数用于获取所有的头部存放到一个map中
     * java.security.Principal 方法参数用于获取在websocket握手阶段使用的用户信息
     * @param allRequestMessage 发送消息参数
     * @throws Exception
     */
    @Override
    public void messageHandlingAll(AllRequestMessage allRequestMessage) throws Exception {
        ResponseMessage response = new ResponseMessage();
        response.setContent(allRequestMessage.getContent());
        response.setType(allRequestMessage.getType());
        response.setSendTime(allRequestMessage.getSendTime());
        response.setSenderId(allRequestMessage.getSenderId());
        messagingTemplate.convertAndSend(stompUrlTopic, response);
    }
}
