package com.springboot.demo.controller;

import com.springboot.demo.rabbitmq.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rabbitmq消息测试
 *
 * @Autor ldq
 * @Date : 2019/9/18 14:08
 */
@RestController
public class RabbitmqController {

    private static Long counter = 0L;

    @Autowired
    private MsgProducer msgProducer;

    @GetMapping("/send")
    public String sendMsg(String content){

        msgProducer.sendMsg(content+counter++);
        return "ok";
    }
}
