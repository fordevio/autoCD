package com.fordevio.producer.services.tasks;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fordevio.producer.models.ThreadStatusModel;

@Service
public class ExecutionThreadStatus {
    private final ConcurrentMap<Long, ThreadStatusModel> threadStatusMap = new ConcurrentHashMap<>(5);
    
    public void put(Long key, ThreadStatusModel value) {
        threadStatusMap.put(key, value);
    }

    public ThreadStatusModel get(Long key) {
        return threadStatusMap.get(key);
    }

    public void remove(Long key) {
        threadStatusMap.remove(key);
    }

    public boolean containsKey(Long key) {
        return threadStatusMap.containsKey(key);
    }

    public List<Long> getKeysWithRunningFalse() {
        return threadStatusMap.entrySet()
                .stream()
                .filter(entry -> Boolean.FALSE.equals(entry.getValue().getRunning()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

}
