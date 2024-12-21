package com.fordevio.producer.controllers;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.models.ProjectExecute;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.ProjectHandler;
import com.fordevio.producer.services.QueueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/protected/deliver")
public class CDController {
 
    @Autowired
    private ProjectHandler projectHandler;

    @Autowired
    private QueueService queueService;
    
    @PostMapping("/{projectName}")
    public ResponseEntity<?> deliverProject(@PathVariable String projectName){
        try{
            Project project = projectHandler.getProjectByName(projectName);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exist"));
            }
            Random random = new Random();
            int randomNumber = 1000 + random.nextInt(9000);
            ProjectExecute projectExecute = new ProjectExecute(randomNumber*1L, project.getName(), new Date());
            queueService.addProjectToQueue(projectExecute);
            log.info("Delivered CD msg for: {}, projectExecuteId: {}", projectName, projectExecute.getId());
            return ResponseEntity.ok(new MessageResponse("Project CD successfully"));
        }catch(Exception e){
            log.warn("Error while delivering project to queue", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }
}
