package com.springboot.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 消息消费者
 *
 * @Autor ldq
 * @Date : 2019/9/17 15:38
 */
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_B)
@Slf4j
public class MsgReceiver {

    @RabbitHandler
    public void process(String msg, Message message, final Channel channel) throws IOException {

        if (!StringUtils.isEmpty(msg)){
            String substring = msg.substring(msg.length() - 1);
            Integer integer = new Integer(substring);
            if (integer % 2 == 0){
                log.info("订单到期自动取消成功："+ msg);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        }
    }
}
