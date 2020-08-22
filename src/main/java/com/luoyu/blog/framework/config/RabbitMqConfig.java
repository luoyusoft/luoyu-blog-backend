package com.luoyu.blog.framework.config;

import com.luoyu.blog.common.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQConfig
 *
 * @author luoyu
 * @date 2019/03/16 21:59
 * @description
 */
@Configuration
public class RabbitMqConfig {

    /**
     *  声明交换机
     */
    @Bean(RabbitMqConstants.EXCHANGE_NAME)
    public Exchange exchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMqConstants.EXCHANGE_NAME).durable(true).build();
    }

    /**
     *  声明队列
     *  new Queue(QUEUE_EMAIL,true,false,false)
     *  durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     *  auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     *  exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE)
    public Queue esQueue() {
        return new Queue(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMqConstants.INIT_LUOYUBLOG_GITALK_QUEUE)
    public Queue gitalkQueue() {
        return new Queue(RabbitMqConstants.INIT_LUOYUBLOG_GITALK_QUEUE);
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEs(@Qualifier(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE) Queue queue,
                             @Qualifier(RabbitMqConstants.EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.REFRESH_ES_INDEX_ROUTINGKEY).noargs();
    }

    /**
     *  INIT_LUOYUBLOG_GITALK_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingGitalk(@Qualifier(RabbitMqConstants.INIT_LUOYUBLOG_GITALK_QUEUE) Queue queue,
                                 @Qualifier(RabbitMqConstants.EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.INIT_LUOYUBLOG_GITALK_ROUTINGKEY).noargs();
    }

}
