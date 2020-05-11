package com.jinhaoxun.blog.framework.tt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 描述 websocket的配置类 开启 ServerPoint
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${stomprabbitmq.relayhost}")
    private String relayHost;
    @Value("${stomprabbitmq.port}")
    private int port;

    /**
     * @author  jinhaoxun
     * @description 注册stomp端点，主要是起到连接作用
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                //端点名称
                .addEndpoint("/websocket")
                //.setHandshakeHandler() //握手处理，主要是连接的时候认证获取其他数据验证等
                //.addInterceptors() //拦截处理，和http拦截类似
                //跨域
                .setAllowedOrigins("*")
                .withSockJS(); //使用sockJS
    }

    /**
     * @author  jinhaoxun
     * @description 注册stomp端点，注册相关服务
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //这里使用的是内存模式，生产环境可以使用rabbitmq或者其他mq。
        //registry.enableStompBrokerRelay().setRelayHost().setRelayPort() 其他方式
        //registry.enableSimpleBroker("/topic");//这里注册两个，主要是目的是将广播和队列分开。
        //registry.setUserDestinationPrefix("/user");//点对点发送前缀
        /*
         * .enableSimpleBroker("/topic","/queue");
         * 假如需要第三方消息代理，比如rabitMQ,activeMq，在这里配置
         */
        log.info("开始注册Stomp到Rabbitmq服务...");
        //设置客户端前缀 即@MessageMapping
        registry.setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/exchange","/topic","/queue","/amq/queue")
                .setRelayHost(relayHost)
                .setRelayPort(port)
                .setClientLogin("admin")
                .setClientPasscode("admin")
                .setSystemLogin("admin")
                .setSystemPasscode("admin")
                .setSystemHeartbeatSendInterval(5000)
                .setSystemHeartbeatReceiveInterval(4000);
        log.info("注册Stomp到Rabbitmq服务成功！");
    }
}