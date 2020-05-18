package com.luoyu.blog.framework.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description RabbitMQ监听器，监听队列QUEUE_A
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_A)
public class RabbitmqMessageListener {

    /**
     * @author  jinhaoxun
     * @description 消费者服务监听器，监听到消息时，要做的事情
     * @param content 监听到的消息
     */
    @RabbitHandler
    public void process(String content) {
        log.info("接收Rabbitmq处理队列A当中的消息： " + content);
    }
}
