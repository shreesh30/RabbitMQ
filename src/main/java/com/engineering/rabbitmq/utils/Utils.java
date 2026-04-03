package com.engineering.rabbitmq.utils;

public class Utils {
    public static final String BURGER_QUEUE = "burger_queue";
    public static final String PIZZA_QUEUE = "pizza_queue";
    public static final String CHUNK_QUEUE = "chunk_queue";
    public static final String RETRY_QUEUE = "retry_queue";
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String CHUNK_EXCHANGE = "chunk_exchange";
    public static final String RETRY_EXCHANGE = "retry_exchange";
    public static final String BURGER_ORDER_CREATED_ROUTING_KEY = "burger.order.created";
    public static final String PIZZA_ORDER_CREATED_ROUTING_KEY = "pizza.order.created";
    public static final String CHUNK_CREATED_ROUTING_KEY = "chunk.created";
    public static final String CHUNK_RETRY_ROUTING_KEY = "chunk.retry";
}
