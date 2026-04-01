package com.engineering.rabbitmq.consumer;

import com.engineering.rabbitmq.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = "order_queue")
    public void receiveOrder(Order order){
        System.out.println("Received: "+order);
    }
}
