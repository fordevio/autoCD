package com.fordevio.producer.services.tasks;

import java.io.IOException;

import com.fordevio.producer.models.ProjectExecute;
import com.fordevio.producer.models.ThreadStatusModel;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptExecutionTask implements Runnable {

    private final Long threadId;

    private QueueService queueService;
   
    private ProjectStatusMap projectStatusMap;

    private ThreadStatusModel threadStatus;

    private ExecutionThreadStatus executionThreadStatus;

    private FileHandlerSvc fileHandler;

    private Long projectId=0*1L;

    private String projectName="";

    private Long projectExecuteId=0*1L;

    public ScriptExecutionTask(Long threadId, QueueService queueService, ProjectStatusMap projectStatusMap,ExecutionThreadStatus executionThreadStatus, FileHandlerSvc fileHandler) {
        this.threadId = threadId;
        this.queueService = queueService;
        this.projectStatusMap = projectStatusMap;
        this.fileHandler = fileHandler;
        this.executionThreadStatus = executionThreadStatus;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try{
              Thread.sleep(100); // 100 ms
              ThreadStatusModel threadStatus=executionThreadStatus.get(threadId);
          
              this.threadStatus = threadStatus;
              threadStatus.setRunning(true);
              executionThreadStatus.put(projectExecuteId, threadStatus);
              ProjectExecute projectExecute = queueService.getProjectFromQueue();

              Boolean isProjectRunning = projectStatusMap.get(projectExecute.getProjectId());
              if(isProjectRunning){
                  log.info("Project is already running, so skipping this project: {}", projectExecute.getProjectName());
                  threadStatus.setRunning(false);
                  executionThreadStatus.put(projectExecuteId, threadStatus);
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
              threadStatus.setRunning(false);
              executionThreadStatus.put(projectExecuteId, threadStatus);
              log.info("Executed script for projec: {}, in thread {}, requested at time: {}, with ID:{}", projectExecute.getProjectName(), threadId, projectExecute.getCreatedDate(), projectExecute.getId());

            }catch(InterruptedException e){
              projectStatusMap.put(this.projectId, false);
              Thread.currentThread().interrupt();
              break;
            }catch(IOException e){
              projectStatusMap.put(this.projectId, false);
              threadStatus.setRunning(false);
              executionThreadStatus.put(projectExecuteId, threadStatus);
              log.error("Error in thread {}, while executing the project: {}, and projectExcutionId: {}",threadId,this.projectName, this.projectExecuteId,e);
            }
        }
    }
}