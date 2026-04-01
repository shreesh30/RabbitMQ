package com.engineering.rabbitmq.controller;

import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class MessageController {
    @Autowired
    private MessageProducer producer;

    @PostMapping("/create")
    public String sendOrder(@RequestBody Order order){
        producer.sendOrder(order);
        return "Order sent";
    }
}
