package com.fordevio.producer.services.tasks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MainExecutionTask implements Runnable {

    private int totalThreads;

    private QueueService queueService;

    public MainExecutionTask(int totalThreads, QueueService queueService){
        this.queueService=queueService;
        this.totalThreads=totalThreads;
    }

    @Override
    public void run(){
      while(!Thread.currentThread().isInterrupted()){
        try{
          int queueSize = queueService.getQueueSize();
          
          if(queueSize>0&&totalThreads==0){
            Thread thread = new Thread(new ScriptExecutionTask(totalThreads+1));
            thread.start();
            totalThreads++;
          }else if(queueSize%(totalThreads*20)>0){
            int threadsToCreate = (queueSize/(totalThreads*20))+1;
            for(int i=0; i<threadsToCreate; i++){
            Thread thread = new Thread(new ScriptExecutionTask(totalThreads+1));
            thread.start();
            totalThreads++;
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
