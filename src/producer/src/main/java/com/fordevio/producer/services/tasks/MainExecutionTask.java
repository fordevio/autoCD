package com.fordevio.producer.services.tasks;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fordevio.producer.models.ThreadStatusModel;
import com.fordevio.producer.services.fileIO.FileHandlerSvc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainExecutionTask implements Runnable {

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
        
          
          if(queueService.getQueueSize()>0&&totalThreads==0){

            Long threadId=randomId();
            Thread thread = new Thread(new ScriptExecutionTask(threadId, queueService, projectStatusMap,executionThreadStatus ,fileHandler));
            thread.start();
            executionThreadStatus.put(threadId, new ThreadStatusModel(thread, false));
            log.info("Thread created with id: {}, queueSize: {}", threadId, queueService.getQueueSize());
            totalThreads++;
            log.info("Total threads: {}, queueSize: {}", totalThreads, queueService.getQueueSize());

          }else if(queueService.getQueueSize()>0&&totalThreads>0&&queueService.getQueueSize()/(totalThreads*20)>0){

            
            Long threadId=randomId();
            Thread thread = new Thread(new ScriptExecutionTask(threadId, queueService, projectStatusMap ,executionThreadStatus,fileHandler));
            
            thread.start();
            executionThreadStatus.put(threadId, new ThreadStatusModel(thread, false));
            log.info("Thread created with id: {}, queueSize: ", threadId, queueService.getQueueSize());
            totalThreads++;
            log.info("Total threads: {}, queueSize: {}", totalThreads, queueService.getQueueSize());

        }else if(queueService.getQueueSize() <= (totalThreads - 1) * 20){
          int threadsToRemove = totalThreads - (int) Math.ceil((double) queueService.getQueueSize() / 20);
          List<Long> keysWithRunningFalse = executionThreadStatus.getKeysWithRunningFalse();
          for(Long id: keysWithRunningFalse){
            if(threadsToRemove==0){
              break;
            }
            ThreadStatusModel threadStatusModel = executionThreadStatus.get(id);
            if(!threadStatusModel.getRunning()){
              executionThreadStatus.remove(id);
              threadStatusModel.getThread().interrupt();
              log.info("Thread removed with id: {}, queueSize: {}", id, queueService.getQueueSize());
              threadsToRemove--;
              totalThreads--;
              log.info("Total threads: {}, queueSize: {}", totalThreads, queueService.getQueueSize());
            }
          }
        }
          Thread.sleep(2000); // 2 seconds
          log.info("Main thread running, total threads: {}, queueSize: {}", totalThreads, queueService.getQueueSize());
        }catch(InterruptedException e){
          log.error("Error in main thread", e);
          Thread.currentThread().interrupt();
        } catch (Exception e) {
          log.error("Error in main thread", e);
        }
      }
    }
  }
