package com.engineering.rabbitmq.controller;

import com.engineering.rabbitmq.model.Chunk;
import com.engineering.rabbitmq.model.Order;
import com.engineering.rabbitmq.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class MessageController {
    @Autowired
    private MessageProducer producer;

    @PostMapping("/create")
    public String sendOrder(@RequestBody Order order){
        if(order.getItem().equals("pizza")){
            for(int i=0;i<10000;i++){
                producer.sendPizzaOrder(order);
            }
        }else{
            for(int i=0;i<10000;i++){
                producer.sendBurgerOrder(order);
            }
        }

        return "Order sent";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws Exception{
        byte[] bytes= file.getBytes();

        int chunkSize=1024*50; //50KB
        int totalChunks = (int) Math.ceil((double)bytes.length/chunkSize);

        String fileId = UUID.randomUUID().toString();


        for(int i=0;i<totalChunks;i++){
            int start = i*chunkSize;
            int end=Math.min(start+chunkSize, bytes.length);

            byte[] chunkData = Arrays.copyOfRange(bytes, start, end);

            Chunk chunk = new Chunk(fileId, i, totalChunks, chunkData);

            producer.sendChunk(chunk);
        }
        return "Image sent in chunks";
    }
}
