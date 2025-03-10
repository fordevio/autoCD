package com.fordevio.producer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.models.User;
import com.fordevio.producer.services.database.ProjectHandler;
import com.fordevio.producer.services.database.UserHandler;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;
import com.fordevio.producer.services.tasks.ExecutionThreadStatus;
import com.fordevio.producer.services.tasks.MainExecutionTask;
import com.fordevio.producer.services.tasks.ProjectStatusMap;
import com.fordevio.producer.services.tasks.QueueService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartRunner {
    
    @Autowired
    private UserHandler userHandler;

    @Autowired
    private FileHandlerSvc fileHandler;

    @Autowired
    private QueueService queueService;

    @Autowired
    private ProjectHandler projectHandler;

    @Autowired
    private ProjectStatusMap projectStatusMap;

    @Autowired
    private ExecutionThreadStatus executionThreadStatus;

    private int totalThreads=0;

    @PostConstruct
    public void init(){
      try{
        createAdminIfNot();
        User admin = userHandler.getAdminUser();
        fileHandler.updateCredentialFile(admin);
        createProjectStatusMap();
        createMainThread();
      }catch(Exception e){
        log.error("Error while init", e);
        throw new RuntimeException("Error while init", e);
      }
    }

    void createAdminIfNot() throws Exception {
        try {
            userHandler.createAdminIfNot();
            fileHandler.createDirIfNot("producerDb");
            fileHandler.createDirIfNot("scripts");
            fileHandler.createDirIfNot("logs");


        } catch (Exception e) {
            log.error("Error while creating admin", e);
            throw new RuntimeException("Error while creating admin", e);
        }
    }

    void createMainThread(){
      Thread thread = new Thread(new MainExecutionTask(totalThreads, queueService, projectStatusMap, executionThreadStatus ,fileHandler));
      thread.start();
    }

    void createProjectStatusMap() throws Exception{
       List<Project> projects = projectHandler.getAllProjects();
        for(Project project: projects){
            projectStatusMap.put(project.getId(), false);
        }
        log.info("Project status map created");
    }

}
