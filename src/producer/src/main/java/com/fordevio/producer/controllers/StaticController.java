package com.fordevio.producer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fordevio.producer.payloads.response.MessageResponse;

@ControllerAdvice
public class StaticController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException ex) {
        String requestedPath = ex.getRequestURL();
        
        return new ResponseEntity<>(new MessageResponse("Route not found"), HttpStatus.NOT_FOUND);
    }
}
