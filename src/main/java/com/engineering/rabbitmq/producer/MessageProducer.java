package com.engineering.rabbitmq.producer;

import com.engineering.rabbitmq.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(Order order){
        rabbitTemplate.convertAndSend("order_queue", order);
        System.out.println("Send: "+order);
    }
}
