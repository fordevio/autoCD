package com.fordevio.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.payloads.requests.AddUpdateProjectRequest;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.ProjectHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/projected/project")
@Slf4j
public class ProjectsController {
  
    @Autowired
    private ProjectHandler projectHandler;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects(){
        try{
        return ResponseEntity.ok(projectHandler.getAllProjects());
        }catch(Exception  e){
           log.warn("Error while getting all projects", e);
           return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@Valid @RequestBody AddUpdateProjectRequest addUpdateProjectRequest){
        try{
            Project isAval = projectHandler.getProjectByName(addUpdateProjectRequest.getName());
            if(isAval != null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project already exists"));
            }
            Project pr = projectHandler.saveProject(new Project(null, addUpdateProjectRequest.getName(), addUpdateProjectRequest.getDescription()));
            return ResponseEntity.ok(pr);

        }catch(Exception e){
            log.warn("Error while adding project", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

}
