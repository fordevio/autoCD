package com.fordevio.producer.services.tasks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class ProjectStatusMap {
    private final ConcurrentMap<Long, Boolean> statusMap = new ConcurrentHashMap<>();
    public void put(Long key, Boolean value) {
        statusMap.put(key, value);
    }

    public Boolean get(Long key) {
        return statusMap.get(key);
    }

    public void remove(Long key) {
        statusMap.remove(key);
    }

    public boolean containsKey(Long key) {
        return statusMap.containsKey(key);
    }
}
