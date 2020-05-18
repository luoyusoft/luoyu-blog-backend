package com.luoyu.blog.project.service.rocketmqservice.impl;

import com.luoyu.blog.project.service.rocketmqservice.IRocketmqService;
import com.luoyu.blog.project.pojo.rocketmq.AddConsumerReq;
import com.luoyu.blog.project.pojo.rocketmq.AddMessageReq;
import com.luoyu.blog.common.response.ResponseFactory;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.framework.rocketmq.RocketmqConsumerFactory;
import com.luoyu.blog.framework.rocketmq.RocketmqProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description RocketMQ服务类
 */
@Slf4j
@Service
public class RocketmqServiceImpl implements IRocketmqService {

    @Autowired
    private RocketmqConsumerFactory rocketmqConsumerFactory;
    @Autowired
    private RocketmqProducerFactory rocketmqProducerFactory;
    @Autowired
    private DefaultMQProducer getRocketmqProducer;
    @Autowired
    private DefaultMQPushConsumer getRocketmqConsumer;

    /**
     * @author jinhaoxun
     * @description 开启消费者
     * @param addConsumerReq 消费者信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult addConsumer(AddConsumerReq addConsumerReq) throws Exception{
        getRocketmqConsumer = rocketmqConsumerFactory.startRocketmqConsumer(addConsumerReq);
        return ResponseFactory.buildSuccessResponse("开启消费者成功！");
    }

    /**
     * @author jinhaoxun
     * @description 关闭消费者
     * @return ResponseResult 成功提示信息
     */
    @Override
    public ResponseResult deleteConsumer(){
        rocketmqConsumerFactory.destroyRocketmqConsumer();
        return ResponseFactory.buildSuccessResponse("关闭消费者成功！");

    }

    /**
     * @author jinhaoxun
     * @description 开启生产者
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult addProducer() throws Exception{
        getRocketmqProducer = rocketmqProducerFactory.getRocketmqProducer();
        return ResponseFactory.buildSuccessResponse("开启生产者成功！");
    }

    /**
     * @author jinhaoxun
     * @description 发送消息
     * @param addMessageReq 发送的消息
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult addMessage(AddMessageReq addMessageReq) throws Exception{
        //创建一条消息对象，指定其主题、标签和消息内容
        Message msg = new Message(
                addMessageReq.getTopic(),
                addMessageReq.getTag(),
                (addMessageReq.getMessage()).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        //发送消息并返回结果
        SendResult sendResult = getRocketmqProducer.send(msg);
        log.info(sendResult.toString());
        return ResponseFactory.buildSuccessResponse("发送消息成功！");
    }

    /**
     * @author jinhaoxun
     * @description 关闭生产者
     * @return ResponseResult 成功提示信息
     */
    @Override
    public ResponseResult deleteProducer(){
        rocketmqProducerFactory.destroyRocketmqProducer();
        return ResponseFactory.buildSuccessResponse("关闭生产者成功！");
    }
}
