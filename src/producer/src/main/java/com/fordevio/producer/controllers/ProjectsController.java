package com.fordevio.producer.controllers;

import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.payloads.requests.AddUpdateProjectRequest;
import com.fordevio.producer.payloads.requests.EditScriptRequest;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.FileHandlerSvc;
import com.fordevio.producer.services.ProjectHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/protected/project")
@Slf4j
public class ProjectsController {
  
    @Autowired
    private ProjectHandler projectHandler;

    @Autowired
    private FileHandlerSvc fileHandler;

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
            fileHandler.createScriptsIfNot(pr.getName());
            fileHandler.createLogsIfNot(pr.getName());
            return ResponseEntity.ok(pr);

        }catch(Exception e){
            log.warn("Error while adding project", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id){
        try{
            Project project = projectHandler.getProjectById(id);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }
            return ResponseEntity.ok(project);
        }catch(Exception e){
            log.warn("Error while getting project", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id,@Valid @RequestBody AddUpdateProjectRequest addUpdateProjectRequest){
        try{
            Project isAval = projectHandler.getProjectById(id);
            if(isAval == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }

            Project isAvalName = projectHandler.getProjectByName(addUpdateProjectRequest.getName());
            if(isAvalName != null && !isAvalName.getId().equals(id)){
                return ResponseEntity.badRequest().body(new MessageResponse("Project already exists"));
            }

            String des = isAval.getDescription();
            if(addUpdateProjectRequest.getDescription() != null){
                des = addUpdateProjectRequest.getDescription();
            }
            Project pr = projectHandler.saveProject(new Project(id, addUpdateProjectRequest.getName(), des));
            if(!isAval.getName().equals(pr.getName())){
                fileHandler.renameScriptFiles(isAval.getName(), pr.getName());
                fileHandler.renameLogFiles(isAval.getName(), pr.getName());
            }
            return ResponseEntity.ok(pr);

        }catch(Exception e){
            log.warn("Error while updating project", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id){
        try{
            Project project = projectHandler.getProjectById(id);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }
            projectHandler.deleteProject(id);
            fileHandler.removeLogFiles(project.getName());
            fileHandler.removeScriptFiles(project.getName());
            return ResponseEntity.ok(new MessageResponse("Project deleted successfully"));
        }catch(Exception e){
            log.warn("Error while deleting project", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/updateScript/{id}")
    public ResponseEntity<?> editProjectScript(@PathVariable Long id, @RequestBody EditScriptRequest editScriptRequest){
        try{
            Project project = projectHandler.getProjectById(id);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }
            fileHandler.editProjectScript(project.getName(), editScriptRequest.getData());
            log.info(project.getName() + " script edited successfully");
            return ResponseEntity.ok(new MessageResponse("Project script edited successfully"));
        }catch(Exception e){
            log.warn("Error while editing project script", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/getScript/{id}")
    public ResponseEntity<?> getProjectScript(@PathVariable Long id){
        try{
            Project project = projectHandler.getProjectById(id);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }
            return ResponseEntity.ok(fileHandler.getProjectScript(project.getName()));
        }catch(Exception e){
            log.warn("Error while getting project script", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/getLogs/{id}")
    public ResponseEntity<?> getProjectLogs(@PathVariable Long id,@RequestParam(required = false, defaultValue = "false") Boolean stream){
        try{
            Project project = projectHandler.getProjectById(id);
            if(project == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Project does not exists"));
            }
            if(!stream){
                return ResponseEntity.ok(fileHandler.getProjectLogs(project.getName()));
            }
            FileInputStream fileInputStream = new FileInputStream(fileHandler.getProjectLogPath(project.getName()));
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(fileInputStream));
        }catch(Exception e){
            log.warn("Error while getting project logs", e);
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }
}
