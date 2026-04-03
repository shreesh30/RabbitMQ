package com.engineering.rabbitmq.config;

import com.engineering.rabbitmq.utils.Utils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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
    public SimpleRabbitListenerContainerFactory factory(ConnectionFactory connectionFactory, JacksonJsonMessageConverter messageConverter){
        SimpleRabbitListenerContainerFactory factory= new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public Queue burgerQueue(){
        return new Queue(Utils.BURGER_QUEUE, true);
    }

    @Bean
    public Queue pizzaQueue(){
        return new Queue(Utils.PIZZA_QUEUE, true);
    }

    @Bean
    public Queue chunkQueue(){
        return new Queue(Utils.CHUNK_QUEUE, true);
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

    @Bean
    public Binding bindingChunk(Queue chunkQueue, DirectExchange exchange){
        return BindingBuilder.bind(chunkQueue).to(exchange).with(Utils.CHUNK_CREATED_ROUTING_KEY);
    }
}
