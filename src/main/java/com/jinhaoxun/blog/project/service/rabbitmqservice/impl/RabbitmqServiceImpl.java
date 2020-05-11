package com.jinhaoxun.blog.project.tt.service.rabbitmqservice.impl;

import com.jinhaoxun.blog.framework.rabbitmq.RabbitmqProducerFactory;
import com.jinhaoxun.blog.project.tt.service.rabbitmqservice.IRabbitmqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description RabbitMQ服务类
 */
@Slf4j
@Service
public class RabbitmqServiceImpl implements IRabbitmqService {

    @Resource
    private RabbitmqProducerFactory rabbitmqProducerFactory;

    /**
     * @author jinhaoxun
     * @description 发送消息方法
     * @param content 发送的消息
     */
    @Override
    public void sendMsg(String content){
        rabbitmqProducerFactory.sendMsg(content);
    }
}
