package com.luoyu.blog.framework.config;

import com.luoyu.blog.common.constants.RabbitMqConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQConfig
 *
 * @author bobbi
 * @date 2019/03/16 21:59
 * @email 571002217@qq.com
 * @description
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue esQueue() {
        return new Queue(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE);
    }

    @Bean
    public Queue gitalkQueue() {
        return new Queue(RabbitMqConstants.INIT_LUOYUBLOG_GITALK_QUEUE);
    }
}
