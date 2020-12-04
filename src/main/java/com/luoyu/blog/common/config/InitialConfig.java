package com.luoyu.blog.common.config;

import com.luoyu.blog.common.util.RabbitMqUtils;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * InitialConfig
 *
 * @author luoyu
 * @date 2019/03/16 23:04
 * @description
 */
@Configuration
@ConditionalOnBean(ElasticsearchClient.class)
public class InitialConfig {

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    /**
     * 项目启动时重新导入索引
     */
    @PostConstruct
    public void initEsIndex(){
//        rabbitmqUtils.send(RabbitMqConstants.LUOYUBLOG_INIT_ES_QUEUE,"luoyublog-search init index");
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setAutomaticRecoveryEnabled(false);
    }

}
