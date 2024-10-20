package com.fordevio.producer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @GetMapping("/api/status")
    public ResponseEntity<?> getStatus(){
        return ResponseEntity.ok("Producer is up and running");
    }
}
