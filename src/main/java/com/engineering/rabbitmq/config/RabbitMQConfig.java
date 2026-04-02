package com.engineering.rabbitmq.config;

import com.engineering.rabbitmq.utils.Utils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue burgerQueue(){
        return new Queue(Utils.BURGER_QUEUE, true);
    }

    @Bean
    public Queue pizzaQueue(){
        return new Queue(Utils.PIZZA_QUEUE, true);
    }


    @Bean
    public JacksonJsonMessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template= new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(Utils.ORDER_EXCHANGE);
    }

    @Bean
    public Binding bindingBurger(Queue burgerQueue, DirectExchange exchange){
        return BindingBuilder.bind(burgerQueue).to(exchange).with(Utils.BURGER_ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPizza(Queue pizzaQueue, DirectExchange exchange){
        return BindingBuilder.bind(pizzaQueue).to(exchange).with(Utils.PIZZA_ORDER_CREATED_ROUTING_KEY);
    }
}
