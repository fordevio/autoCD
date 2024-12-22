package com.fordevio.producer.services.tasks;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.stereotype.Service;

import com.fordevio.producer.models.ProjectExecute;

@Service
public class QueueService {
    
    private final ArrayBlockingQueue<ProjectExecute> projectQueue=new ArrayBlockingQueue<>(100);

    public void addProjectToQueue(ProjectExecute projectExecute) throws InterruptedException {

        projectQueue.put(projectExecute);
    }
    
    public ProjectExecute getProjectFromQueue() throws InterruptedException{
        return projectQueue.take();
    }

    public boolean isQueueEmpty() {
        return projectQueue.isEmpty();
    }

    public int getQueueSize() {    
        return projectQueue.size();
    }
}
