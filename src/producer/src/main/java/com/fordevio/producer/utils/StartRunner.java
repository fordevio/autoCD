package com.fordevio.producer.utils;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class StartRunner {
    
    @PostConstruct
    public void createAdmin(){
        System.out.println("Admin created so start");
    }
}
