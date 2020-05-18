package com.luoyu.blog.framework.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description RabbitMQ配置
 *  Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 *  Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 *  Queue:消息的载体,每个消息都会被投到一个或多个队列。
 *  Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 *  Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 *  vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 *  Producer:消息生产者,就是投递消息的程序.
 *  Consumer:消息消费者,就是接受消息的程序.
 *  Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Slf4j
@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";

    @Value("${rabbitmq.host}")
    private String addresses;
    @Value("${rabbitmq.port}")
    private int port;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.publisher-confirms}")
    private Boolean publisherConfirms;
    @Value("${rabbitmq.publisher-returns}")
    private Boolean publisherReturns;
    @Value("${rabbitmq.virtual-host}")
    private String virtualHost;

    /**
     * @author  jinhaoxun
     * @description 构建mq实例工厂
     * @return ConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("开始构建Rabbitmq实例工厂...");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherReturns(publisherReturns);
        log.info("构建Rabbitmq实例工厂成功！");
        return connectionFactory;
    }

    /**
     * @author  jinhaoxun
     * @description RabbitTemplate必须是prototype类型
     * @return RabbitTemplate
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    /**
     * @author  jinhaoxun
     * @description 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     * @return DirectExchange
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    /**
     * @author  jinhaoxun
     * @description 获取队列A
     * @return Queue
     */
    @Bean
    public Queue queueAq() {
        //队列持久
        return new Queue(QUEUE_A, true);
    }

    /**
     * @author  jinhaoxun
     * @description 获取队列B
     * @return Queue
     */
    @Bean
    public Queue queueBq() {
        //队列持久
        return new Queue(QUEUE_B, true);
    }

    /**
     * @author  jinhaoxun
     * @description 通过路由关键字进行绑定
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueAq()).to(defaultExchange()).with(RabbitmqConfig.ROUTINGKEY_A);
    }

    /**
     * @author  jinhaoxun
     * @description 一个交换机可以绑定多个消息队列，也就是消息通过一个交换机，可以分发到不同的队列当中去。
     * @return Binding
     */
    @Bean
    public Binding bindingBb(){
        return BindingBuilder.bind(queueBq()).to(defaultExchange()).with(RabbitmqConfig.ROUTINGKEY_B);
    }

    /**
     * @author  jinhaoxun
     * @description 另外一种消息处理机制的写法如下
     * @return SimpleMessageListenerContainer
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        //加载处理消息A的队列
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        //设置接收多个队列里面的消息，这里设置接收队列A
        //假如想一个消费者处理多个队列里面的信息可以如下设置：
        // container.setQueues(queueA(),queueB(),queueC());
        container.setQueues(queueAq());
        container.setExposeListenerChannel(true);
        //设置最大的并发的消费者数量
        container.setMaxConcurrentConsumers(10);
        //最小的并发消费者的数量
        container.setConcurrentConsumers(1);
        //设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            /*
             * 通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，
             * 换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
             */
            channel.basicQos(1);
            byte[] body = message.getBody();
            log.info("接收处理队列A当中的消息:" + new String(body));
            /*
             * 为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
             * 当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        });
        return container;
    }
}