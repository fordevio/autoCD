package com.fordevio.producer.services.tasks;

import java.io.IOException;

import com.fordevio.producer.models.ProjectExecute;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptExecutionTask implements Runnable {

    private final int threadNumber;

    private QueueService queueService;
   
    private ProjectStatusMap projectStatusMap;

    private FileHandlerSvc fileHandler;

    private Long projectId=0*1L;

    private String projectName="";

    private Long projectExecuteId=0*1L;

    public ScriptExecutionTask(int threadNumber, QueueService queueService, ProjectStatusMap projectStatusMap, FileHandlerSvc fileHandler) {
        this.threadNumber = threadNumber;
        this.queueService = queueService;
        this.projectStatusMap = projectStatusMap;
        this.fileHandler = fileHandler;
    }

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