package com.engineering.rabbitmq.consumer;

import com.engineering.rabbitmq.model.Chunk;
import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.utils.Utils;
import com.rabbitmq.client.Channel;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = Utils.CHUNK_QUEUE, containerFactory = "factory")
    public void receiveChunks(Chunk chunk, Channel channel, Message message) throws IOException {
        System.out.println("Receiving chunks");

        List<Map<String, ?>> xDeath = message.getMessageProperties().getXDeathHeader();

        int retryCount = 0;

        if (xDeath != null && !xDeath.isEmpty()) {
            Object countObj = xDeath.get(0).get("count");

            if (countObj instanceof Long) {
                retryCount = ((Long) countObj).intValue();
            }
        }

        try {
            System.out.println("retry count: "+retryCount);

            if(retryCount<=2) {
                int failure = 1 / 0;
            }

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
            System.out.println(e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }

    }
}
