package com.springboot.demo.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *
 * @Autor ldq
 * @Date : 2019/9/17 15:38
 */
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_A)
@Slf4j
public class MsgReceiver {

    @RabbitHandler
    public void process(String msg){
        log.info("接收队列A的消息："+ msg);
    }
}
