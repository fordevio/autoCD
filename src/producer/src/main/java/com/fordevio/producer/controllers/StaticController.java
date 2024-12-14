package com.fordevio.producer.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticController {

    private final ResourceLoader resourceLoader;

    public StaticController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @GetMapping(value = "/{path:^(?!api)(?!.*\\.)(?!$).*}")
    public ResponseEntity<?> redirect() {
        Resource indexHtml = resourceLoader.getResource("classpath:/static/index.html");
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(indexHtml);
    }
}
