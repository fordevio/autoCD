package com.fordevio.producer.services.tasks;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fordevio.producer.models.ThreadStatusModel;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MainExecutionTask implements Runnable {

    private int totalThreads;

    private QueueService queueService;

    private ProjectStatusMap projectStatusMap;

    private ExecutionThreadStatus executionThreadStatus;

    private FileHandlerSvc fileHandler;

    public MainExecutionTask(int totalThreads, QueueService queueService, ProjectStatusMap projectStatusMap,ExecutionThreadStatus executionThreadStatus ,FileHandlerSvc fileHandlerSvc){
        this.queueService=queueService;
        this.totalThreads=totalThreads;
        this.fileHandler=fileHandlerSvc;
        this.projectStatusMap=projectStatusMap;
        this.executionThreadStatus=executionThreadStatus;
    }

    private static Long randomId(){
      return  ThreadLocalRandom.current().nextInt(1000, 10000)*1L;
    }

    @Override
    public void run(){
      while(!Thread.currentThread().isInterrupted()){
        try{
          int queueSize = queueService.getQueueSize();
          
          if(queueSize>0&&totalThreads==0){
            Long threadId=randomId();
            Thread thread = new Thread(new ScriptExecutionTask(threadId, queueService, projectStatusMap,executionThreadStatus ,fileHandler));
            thread.start();
            executionThreadStatus.put(threadId, new ThreadStatusModel(thread, false));
            totalThreads++;
          }else if(queueSize%(totalThreads*20)>0){
            int threadsToCreate = (queueSize/(totalThreads*20))+1;
            for(int i=0; i<threadsToCreate; i++){
            Long threadId=randomId();
            Thread thread = new Thread(new ScriptExecutionTask(threadId, queueService, projectStatusMap ,executionThreadStatus,fileHandler));
            thread.start();
            executionThreadStatus.put(threadId, new ThreadStatusModel(thread, false));
            totalThreads++;
          }
        }else if(queueSize <= (totalThreads - 1) * 20){
          int threadsToRemove = totalThreads - (int) Math.ceil((double) queueSize / 20);
          List<Long> keysWithRunningFalse = executionThreadStatus.getKeysWithRunningFalse();
          for(Long id: keysWithRunningFalse){
            if(threadsToRemove==0){
              break;
            }
            ThreadStatusModel threadStatusModel = executionThreadStatus.get(id);
            if(!threadStatusModel.getRunning()){
              threadStatusModel.getThread().interrupt();
              executionThreadStatus.remove(id);
              threadsToRemove--;
              totalThreads--;
            }
          }
        }
          Thread.sleep(5);

        }catch(InterruptedException e){
          log.error("Error in main thread", e);
          Thread.currentThread().interrupt();
        } catch (Exception e) {
          log.error("Error in main thread", e);
        }
      }
    }
  }
