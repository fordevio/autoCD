package com.fordevio.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.ProjectHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/protected/deliver")
public class CDController {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
 
    @Autowired
    private ProjectHandler projectHandler;

    public void sendMessage(String key,String msg) {
      kafkaTemplate.send("autocd",key, msg);
    }   
    
    @PostMapping("/{projectName}")
    public ResponseEntity<?> deliverProject(@PathVariable String projectName){
        try{
            Project project = projectHandler.getProjectByName(projectName);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exist"));
            }
            String scriptFilePath = "/var/autocd/scripts/"+projectName+".sh";
             String logFilePath = "/var/autocd/logs/"+projectName+".log";
            log.info("Delivered CD msg for: {}", projectName);
            return ResponseEntity.ok(new MessageResponse("Project CD successfully"));
        }catch(Exception e){
            log.warn("Error while getting project logs", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }
}
