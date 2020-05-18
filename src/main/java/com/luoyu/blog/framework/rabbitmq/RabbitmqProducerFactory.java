package com.luoyu.blog.framework.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description RabbitMQ生产者
 */
@Slf4j
@Component
public class RabbitmqProducerFactory implements RabbitTemplate.ConfirmCallback {

    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
     */
    private RabbitTemplate rabbitTemplate;

    /**
     * @author  jinhaoxun
     * @description 构造方法注入rabbitTemplate
     * @param  rabbitTemplate
     * @return  RabbitMQProducerFactory
     */
    @Autowired
    public RabbitmqProducerFactory(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * @author  jinhaoxun
     * @description 发送消息方法
     * @param  content 发送的消息内容
     * @return  RabbitMQProducerFactory
     */
    public void sendMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_A, RabbitmqConfig.ROUTINGKEY_A, content, correlationId);
    }

    /**
     * @author  jinhaoxun
     * @description 发送消息回调方法
     * @param  correlationData 回调id
     * @param  ack 消息是否成功消费
     * @param  cause 消息消费失败的错误信息
     * @return  RabbitMQProducerFactory
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + cause);
        }
    }
}
