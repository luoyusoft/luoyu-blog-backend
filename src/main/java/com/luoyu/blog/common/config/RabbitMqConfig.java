package com.luoyu.blog.common.config;

import com.luoyu.blog.common.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * RabbitMQConfig
 * @author luoyu
 * @date 2019/03/16 21:59
 * @description
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     *  声明交换机
     */
    @Bean(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE)
    public Exchange exchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     *  声明队列
     *  new Queue(QUEUE_EMAIL,true,false,false)
     *  durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     *  auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     *  exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean(RabbitMqConstants.LUOYUBLOG_ES_ADD_QUEUE)
    public Queue esAddQueue() {
        return new Queue(RabbitMqConstants.LUOYUBLOG_ES_ADD_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMqConstants.LUOYUBLOG_ES_UPDATE_QUEUE)
    public Queue esUpdateQueue() {
        return new Queue(RabbitMqConstants.LUOYUBLOG_ES_UPDATE_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMqConstants.LUOYUBLOG_ES_DELETE_QUEUE)
    public Queue esDeleteQueue() {
        return new Queue(RabbitMqConstants.LUOYUBLOG_ES_DELETE_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMqConstants.LUOYUBLOG_INIT_GITALK_QUEUE)
    public Queue gitalkQueue() {
        return new Queue(RabbitMqConstants.LUOYUBLOG_INIT_GITALK_QUEUE);
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsAdd(@Qualifier(RabbitMqConstants.LUOYUBLOG_ES_ADD_QUEUE) Queue queue,
                             @Qualifier(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_ES_ROUTINGKEY_ADD).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsUpdate(@Qualifier(RabbitMqConstants.LUOYUBLOG_ES_UPDATE_QUEUE) Queue queue,
                             @Qualifier(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_ES_ROUTINGKEY_UPDATE).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsDelete(@Qualifier(RabbitMqConstants.LUOYUBLOG_ES_DELETE_QUEUE) Queue queue,
                             @Qualifier(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_ES_ROUTINGKEY_DELETE).noargs();
    }

    /**
     *  INIT_LUOYUBLOG_GITALK_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingGitalk(@Qualifier(RabbitMqConstants.LUOYUBLOG_INIT_GITALK_QUEUE) Queue queue,
                                 @Qualifier(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY).noargs();
    }

    /**
     * 如果需要在生产者需要消息发送后的回调，
     * 需要对rabbitTemplate设置ConfirmCallback对象，
     * 由于不同的生产者需要对应不同的ConfirmCallback，
     * 如果rabbitTemplate设置为单例bean，
     * 则所有的rabbitTemplate实际的ConfirmCallback为最后一次申明的ConfirmCallback。
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }

}
