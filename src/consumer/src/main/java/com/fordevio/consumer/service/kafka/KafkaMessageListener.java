package com.fordevio.consumer.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "autocd", groupId = "autocd-consumer")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
