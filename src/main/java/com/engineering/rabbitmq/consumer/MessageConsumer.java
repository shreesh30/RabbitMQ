package com.engineering.rabbitmq.consumer;

import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.utils.Utils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = {Utils.BURGER_QUEUE, Utils.PIZZA_QUEUE})
    public void receiveOrder(Order order){
        System.out.println("Received: "+order);
    }
}
