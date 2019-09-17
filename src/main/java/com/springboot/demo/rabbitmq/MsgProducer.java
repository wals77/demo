package com.springboot.demo.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 消息生产者
 *
 * @Autor ldq
 * @Date : 2019/9/17 14:49
 */
@Component
@Slf4j
public class MsgProducer implements RabbitTemplate.ConfirmCallback {


    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMsg(String content){
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_A, RabbitmqConfig.ROUTINGKEY_A, content, data);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            log.info("消费成功");
        } else {
            log.error("消费失败:", cause);
        }
    }
}
