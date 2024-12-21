package com.fordevio.producer.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.models.ProjectExecute;

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
        executorService.submit(new ScriptExecutionTask(i));
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

    class ScriptExecutionTask implements Runnable {

      private final int threadNumber;

      private Long projectId=0*1L;

      private String projectName="";

      private Long projectExecuteId=0*1L;

      public ScriptExecutionTask(int threadNumber) {
          this.threadNumber = threadNumber;
      }

      @Override
      public void run() {
          while(!Thread.currentThread().isInterrupted()){
              try{
                ProjectExecute projectExecute = queueService.getProjectFromQueue();
                Boolean isProjectRunning = projectStatusMap.get(projectExecute.getProjectId());
                if(isProjectRunning){
                    log.info("Project is already running, so skipping this project: {}", projectExecute.getProjectName());
                    continue;
                }
                projectStatusMap.put(projectExecute.getProjectId(), true);
                this.projectId = projectExecute.getProjectId();
                this.projectName = projectExecute.getProjectName();
                this.projectExecuteId = projectExecute.getId();
                String scriptFilePath = "/var/autocd/scripts/"+projectExecute.getProjectId()+".sh";
                String logFilePath = "/var/autocd/logs/"+projectExecute.getProjectId()+".log";
                fileHandler.executeShellScript(scriptFilePath, logFilePath);    
                projectStatusMap.put(projectExecute.getProjectId(), false); 
                log.info("Executed script for projec: {}, in thread {}, requested at time: {}, with ID:{}", projectExecute.getProjectName(), threadNumber, projectExecute.getCreatedDate(), projectExecute.getId());

              }catch(InterruptedException e){
                log.error("Error in thread {}",threadNumber, e);
                projectStatusMap.put(this.projectId, false);
                Thread.currentThread().interrupt();
                break;
              }catch(IOException e){
                projectStatusMap.put(this.projectId, false);
                log.error("Error in thread {}, while executing the project: {}, and projectExcutionId: {}",threadNumber,this.projectName, this.projectExecuteId,e);
              }
          }
      }
  }


}
