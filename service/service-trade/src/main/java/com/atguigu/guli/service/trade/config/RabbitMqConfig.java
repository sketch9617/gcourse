package com.atguigu.guli.service.trade.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;
@Slf4j
@Configuration
public class RabbitMqConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;
    //配置生产者确认 回调
    @PostConstruct
    public void init(){
        //消息是否到达交换机的回调：
        rabbitTemplate.setConfirmCallback((@Nullable CorrelationData correlationData,
                                           boolean ack, @Nullable String cause)->{
            if(!ack){//消息未到达交换机
                log.error("消息未到达交换机：{}",cause);
            }

        });

        //消息未到达队列时的回调：
        rabbitTemplate.setReturnCallback((Message message, int replyCode,
                                          String replyText, String exchange, String routingKey)->{
            //确保消息能够正常到达交换机或者队列
            log.error("消息未到达队列：replyCode = {} , replyText = {} ,exchange = {} ,routingKey ={} ， msg = {}"
                    ,replyCode,replyText , exchange, routingKey , new String(message.getBody()));
        });

    }


}
