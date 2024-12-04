package com.fordevio.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliver")
public class KafkaController {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
 
    public void sendMessage(String topicName,String msg) {
      kafkaTemplate.send(topicName, msg);
    }   
    
    @PostMapping("/{projectName}")
    public void deliverProject(String projectName){
        sendMessage(projectName, projectName);
    }

    

}
