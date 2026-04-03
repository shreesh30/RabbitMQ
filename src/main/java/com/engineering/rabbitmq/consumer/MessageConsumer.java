package com.engineering.rabbitmq.consumer;

import com.engineering.rabbitmq.model.Chunk;
import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.utils.Utils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageConsumer {

    private Map<String, List<Chunk>> storage = new ConcurrentHashMap<>();

    @RabbitListener(queues = {Utils.BURGER_QUEUE, Utils.PIZZA_QUEUE})
    public void receiveOrder(Order order){
        System.out.println("Received: "+order);
    }

    @RabbitListener(queues = Utils.CHUNK_QUEUE, containerFactory = "factory")
    public void receiveChunks(Chunk chunk, Channel channel, Message message) throws IOException {
        try {
            storage.putIfAbsent(chunk.getFileId(), new ArrayList<>());
            storage.get(chunk.getFileId()).add(chunk);

            List<Chunk> chunks = storage.get(chunk.getFileId());

            if (chunks.size() == chunk.getTotalChunks()) {
                chunks.sort(Comparator.comparing(Chunk::getChunkIndex));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                for (Chunk c : chunks) {
                    outputStream.write(c.getData());
                }

                byte[] fullImage = outputStream.toByteArray();

                Files.write(Paths.get("output-" + chunk.getFileId() + ".jpg"), fullImage);

                System.out.println("Image reconstructued");

                storage.remove(chunk.getFileId());
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }

    }
}
