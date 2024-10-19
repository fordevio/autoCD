package com.fordevio.producer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @GetMapping("/api/status")
    public String getStatus(){
        return "API is working";
    }
}
