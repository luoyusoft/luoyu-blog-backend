package com.luoyu.blog.common.util;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RabbitMqUtils
 *
 * @author bobbi
 * @date 2019/03/16 22:08
 * @email 571002217@qq.com
 * @description
 */
@Component
public class RabbitMqUtils {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 发送到指定Queue
     * @param queueName
     * @param obj
     */
    public void send(String queueName, Object obj){
        this.rabbitTemplate.convertAndSend(queueName, obj);
    }

    /**
     * 1、交换机名称
     * 2、routingKey
     * 3、消息内容
     */
    public void sendByRoutingKey(String exChange, String routingKey, Object obj){
        this.rabbitTemplate.convertAndSend(exChange, routingKey, obj);
    }
}
