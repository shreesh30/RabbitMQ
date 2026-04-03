package com.engineering.rabbitmq.config;

import com.engineering.rabbitmq.utils.Utils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public JacksonJsonMessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory("localhost");
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setPort(5671);
        return connectionFactory;
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
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", Utils.RETRY_EXCHANGE);
        args.put("x-dead-letter-routing-key", Utils.CHUNK_RETRY_ROUTING_KEY);

        return new Queue(Utils.CHUNK_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue retryQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 30000);
        args.put("x-dead-letter-exchange", Utils.CHUNK_EXCHANGE);
        args.put("x-dead-letter-routing-key", Utils.CHUNK_CREATED_ROUTING_KEY);

        return new Queue(Utils.RETRY_QUEUE, true, false, false, args);
    }


    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(Utils.ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange chunkExchange(){
        return new DirectExchange(Utils.CHUNK_EXCHANGE);
    }

    @Bean
    public DirectExchange retryExchange(){
        return new DirectExchange(Utils.RETRY_EXCHANGE);
    }

    @Bean
    public Binding bindingBurger(Queue burgerQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(burgerQueue).to(orderExchange).with(Utils.BURGER_ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPizza(Queue pizzaQueue, DirectExchange orderExchange){
        return BindingBuilder.bind(pizzaQueue).to(orderExchange).with(Utils.PIZZA_ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingChunk(Queue chunkQueue, DirectExchange chunkExchange){
        return BindingBuilder.bind(chunkQueue).to(chunkExchange).with(Utils.CHUNK_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingRetry(Queue retryQueue, DirectExchange retryExchange){
        return BindingBuilder.bind(retryQueue).to(retryExchange).with(Utils.CHUNK_RETRY_ROUTING_KEY);
    }
}
