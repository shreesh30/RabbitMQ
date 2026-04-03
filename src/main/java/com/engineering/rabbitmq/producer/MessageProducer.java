package com.engineering.rabbitmq.producer;

import com.engineering.rabbitmq.model.Chunk;
import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.utils.Utils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendBurgerOrder(Order order){
        System.out.println("Start sendBurgerOrder");
        rabbitTemplate.convertAndSend(Utils.ORDER_EXCHANGE, Utils.BURGER_ORDER_CREATED_ROUTING_KEY, order);
        System.out.println("Send: "+order);
    }

    public void sendPizzaOrder(Order order){
        System.out.println("Start sendPizzaOrder");
        rabbitTemplate.convertAndSend(Utils.ORDER_EXCHANGE, Utils.PIZZA_ORDER_CREATED_ROUTING_KEY, order);
        System.out.println("Send: "+order);
    }

    public void sendChunk(Chunk chunk){
        System.out.println("Start sendChunk");
        rabbitTemplate.convertAndSend(Utils.CHUNK_EXCHANGE, Utils.CHUNK_CREATED_ROUTING_KEY, chunk);
        System.out.println("Sent: "+chunk);
    }
}
