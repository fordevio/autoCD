package com.fordevio.producer.services;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.services.database.ProjectHandler;
import com.fordevio.producer.services.database.UserHandler;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;
import com.fordevio.producer.services.tasks.ProjectStatusMap;
import com.fordevio.producer.services.tasks.QueueService;
import com.fordevio.producer.services.tasks.ScriptExecutionTask;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartRunner {
    
  private final ExecutorService executorService = Executors.newFixedThreadPool(7);
    
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

    private int totalThreads=0;

    @PostConstruct
    public void init(){
      try{
        createAdminIfNot();
        createProjectStatusMap();
        createThreadsForScriptExecution();
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

    void createThreadsForScriptExecution(){
      for (int i = 0; i < 7; i++) {
        executorService.submit(new ScriptExecutionTask(i, queueService, projectStatusMap, fileHandler));
        log.info("Thread {} started", i);
    }
    }

    void createProjectStatusMap() throws Exception{
       List<Project> projects = projectHandler.getAllProjects();
        for(Project project: projects){
            projectStatusMap.put(project.getId(), false);
        }
        log.info("Project status map created");
    }

    
    


}
