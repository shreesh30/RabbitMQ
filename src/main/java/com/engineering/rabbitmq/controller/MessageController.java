package com.engineering.rabbitmq.controller;

import com.engineering.rabbitmq.model.Chunk;
import com.engineering.rabbitmq.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageProducer producer;

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
