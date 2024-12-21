package com.fordevio.producer.services;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @PostConstruct
    public void init(){
      try{
        createAdminIfNot();
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
            log.info("Admin created successfully");
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

    class ScriptExecutionTask implements Runnable {

       private final int threadNumber;

        public ScriptExecutionTask(int threadNumber) {
            this.threadNumber = threadNumber;
        }

      @Override
      public void run() {
          while(!Thread.currentThread().isInterrupted()){
              try{
                ProjectExecute project= queueService.getProjectFromQueue();
                String scriptFilePath = "/var/autocd/scripts/"+project.getProjectName()+".sh";
                String logFilePath = "/var/autocd/logs/"+project.getProjectName()+".log";
                fileHandler.executeShellScript(scriptFilePath, logFilePath);      
                log.info("Executed script for projec: {}, in thread {}, requested at time: {}, with ID:{}", project.getProjectName(), threadNumber, project.getCreatedDate());;
              }catch(InterruptedException e){
                log.error("Error in thread {}",threadNumber, e);
                Thread.currentThread().interrupt();
                break;
              }catch(IOException e){
                log.error("Error in thread {}, while executing the project:",threadNumber, e);
            }
          }
      }
  }


}
