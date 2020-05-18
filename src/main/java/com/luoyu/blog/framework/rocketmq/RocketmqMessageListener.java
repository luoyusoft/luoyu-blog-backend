package com.luoyu.blog.framework.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Rocketmq消费者监听类
 */
@Slf4j
public class RocketmqMessageListener implements MessageListenerConcurrently {

    /**
     * 自定义UTF_8
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * @author  jinhaoxun
     * @description 消费者服务监听器，监听到消息时，要做的事情
     * @param list 监听到的消息队列
     * @param consumeConcurrentlyContext
     * @return ConsumeConcurrentlyStatus 返回消费状态给RocketMq
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
            for (MessageExt ext : list) {
                try {
                    log.info("监听到RocketMQ消息 : " + new String(ext.getBody(), UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
