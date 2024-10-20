package com.fordevio.producer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fordevio.producer.services.UserHandler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartRunner {
    

    @Autowired
    private UserHandler userHandler;

    @PostConstruct
    public void createAdminIfNot(){
        userHandler.createAdminIfNot();
    }
}
