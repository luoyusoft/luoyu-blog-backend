package com.luoyu.blog.common.config;

import com.luoyu.blog.common.constants.RabbitMQConstants;
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
public class RabbitMQConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /********************** article ********************************/

    /**
     *  声明交换机
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE)
    public Exchange articleTopicExchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     *  声明队列
     *  new Queue(QUEUE_EMAIL,true,false,false)
     *  durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     *  auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     *  exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_ADD_QUEUE)
    public Queue esArticleAddQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_ADD_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_UPDATE_QUEUE)
    public Queue esArticleUpdateQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_UPDATE_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_DELETE_QUEUE)
    public Queue esArticleDeleteQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_DELETE_QUEUE);
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsArticleAdd(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_ADD_QUEUE) Queue queue,
                                       @Qualifier(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_ARTICLE_ADD_ROUTINGKEY).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsArticleUpdate(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_UPDATE_QUEUE) Queue queue,
                                          @Qualifier(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_ARTICLE_UPDATE_ROUTINGKEY).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsArticleDelete(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_DELETE_QUEUE) Queue queue,
                                          @Qualifier(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_ARTICLE_DELETE_ROUTINGKEY).noargs();
    }

    /********************** video ********************************/

    /**
     *  声明交换机
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE)
    public Exchange videoTopicExchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_ADD_QUEUE)
    public Queue esVideoAddQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_ADD_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_UPDATE_QUEUE)
    public Queue esVideoUpdateQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_UPDATE_QUEUE);
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_DELETE_QUEUE)
    public Queue esVideoDeleteQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_DELETE_QUEUE);
    }


    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsVideoAdd(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_ADD_QUEUE) Queue queue,
                                     @Qualifier(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_VIDEO_ADD_ROUTINGKEY).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsVideoUpdate(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_UPDATE_QUEUE) Queue queue,
                                        @Qualifier(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_VIDEO_UPDATE_ROUTINGKEY).noargs();
    }

    /**
     *  REFRESH_ES_INDEX_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingEsVideoDelete(@Qualifier(RabbitMQConstants.LUOYUBLOG_ES_VIDEO_DELETE_QUEUE) Queue queue,
                                        @Qualifier(RabbitMQConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_ES_VIDEO_DELETE_ROUTINGKEY).noargs();
    }


    /********************** gitalk ********************************/

    /**
     *  声明交换机
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE)
    public Exchange gitalkTopicExchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     *  声明队列
     */
    @Bean(RabbitMQConstants.LUOYUBLOG_GITALK_INIT_QUEUE)
    public Queue gitalkInitQueue() {
        return new Queue(RabbitMQConstants.LUOYUBLOG_GITALK_INIT_QUEUE);
    }

    /**
     *  INIT_LUOYUBLOG_GITALK_QUEUE队列绑定交换机，指定routingKey
     */
    @Bean
    public Binding bindingGitalk(@Qualifier(RabbitMQConstants.LUOYUBLOG_GITALK_INIT_QUEUE) Queue queue,
                                 @Qualifier(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TOPIC_GITALK_ROUTINGKEY).noargs();
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
