package com.jinhaoxun.blog.project.tt.service.websocketservice;

import com.jinhaoxun.blog.project.tt.pojo.websocket.all.AllRequestMessage;
import com.jinhaoxun.blog.project.tt.pojo.websocket.chat.ChatRequestMessage;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description WebSocket服务接口
 */
public interface IWebSocketService {

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
    void messageHandling(ChatRequestMessage requestMessage) throws Exception;

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
    void messageHandlingAll(AllRequestMessage allRequestMessage) throws Exception;
}
