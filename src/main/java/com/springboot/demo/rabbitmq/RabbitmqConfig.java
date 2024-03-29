package com.springboot.demo.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

/**
 * rabbitmq 配置
 * Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 * Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 * Queue:消息的载体,每个消息都会被投到一个或多个队列。
 * Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 * Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 * vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 * Producer:消息生产者,就是投递消息的程序.
 * Consumer:消息消费者,就是接受消息的程序.
 * Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 * @Autor ldq
 * @Date : 2019/9/17 12:05
 */
@Configuration
public class RabbitmqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String DEAD_EXCHANGE_FOR_A = "dead-exchange-for_A";
    public static final String DEAD_EXCHANGE_FOR_B = "dead_exchange_for_B";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_A);
    }

    @Bean
    public DirectExchange directDeadExchange(){
        return new DirectExchange(DEAD_EXCHANGE_FOR_A);
    }

    @Bean
    public DirectExchange directDeadExchangeB(){
        return new DirectExchange(DEAD_EXCHANGE_FOR_B);
    }

    @Bean
    public Queue queue(){
        HashMap arguments = new HashMap();
        arguments.put("x-message-ttl",10000);   //消息过期10秒

        //这两个参数必须都写
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_FOR_A);    //死信交换器
        arguments.put("x-dead-letter-routing-key",ROUTINGKEY_B);    //死信路由

        return new Queue(QUEUE_A,true,false,false, arguments);
    }

    @Bean
    public Queue queueB(){
        HashMap arguments = new HashMap();
        //这两个参数必须都写
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE_FOR_B);    //死信交换器
        arguments.put("x-dead-letter-routing-key",ROUTINGKEY_C);    //死信路由
        return new Queue(QUEUE_B,true,false,false, arguments);
    }

    @Bean
    public Queue queueC(){
        return new Queue(QUEUE_C);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding binding_deadB(){  //死信交换器绑定队列
        return BindingBuilder.bind(queueB()).to(directDeadExchange()).with(ROUTINGKEY_B);
    }

    @Bean
    public Binding binding_deadC(){  //死信交换器绑定队列
        return BindingBuilder.bind(queueC()).to(directDeadExchangeB()).with(ROUTINGKEY_C);
    }
}
