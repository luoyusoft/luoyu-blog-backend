package com.luoyu.blog.framework.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Rocketmq生成者工厂类
 */
@Slf4j
@Component
public class RocketmqProducerFactory {

    @Value("${rocketmq.producer.groupname}")
    private String producerGroupName;
    @Value("${rocketmq.namesrvaddr}")
    private String nameServerAddr;
    @Value("${rocketmq.producer.maxmessagesize}")
    private int maxMessageSize;
    @Value("${rocketmq.producer.sendmsgtimeout}")
    private int sendMsgTimeout;
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * @author  jinhaoxun
     * @description 配置DefaultMQProducer，启动项目时默认生产者服务
     * @return DefaultMQProducer
     */
    @Bean
    public DefaultMQProducer getRocketmqProducer() throws Exception {
        log.info("开始启动Rocketmq生产者服务...");
        //创建一个消息生产者，并设置一个消息生产者组
        defaultMQProducer = new DefaultMQProducer(producerGroupName);
        //指定 NameServer 地址
        defaultMQProducer.setNamesrvAddr(nameServerAddr);
        defaultMQProducer.setMaxMessageSize(maxMessageSize);
        defaultMQProducer.setSendMsgTimeout(sendMsgTimeout);
        defaultMQProducer.setVipChannelEnabled(false);
        //初始化 SpringProducer，整个应用生命周期内只需要初始化一次
        defaultMQProducer.start();
        log.info("Rocketmq生产者服务启动成功！");
        return defaultMQProducer;
    }

    /**
     * @author  jinhaoxun
     * @description 关闭生产者服务
     */
    public void destroyRocketmqProducer() {
        log.info("开始关闭Rocketmq生产者服务...");
        defaultMQProducer.shutdown();
        log.info("Rocketmq生产者服务已关闭！");
    }
}
