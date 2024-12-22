package com.fordevio.producer.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.User;
import com.fordevio.producer.payloads.requests.LoginRequest;
import com.fordevio.producer.payloads.response.LoginResponse;
import com.fordevio.producer.services.database.UserHandler;
import com.fordevio.producer.services.security.jwt.JwtUtils;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserHandler userHandler;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody  LoginRequest loginRequest){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            String jwt = jwtUtils.generateJwtToken(authentication,28800000);
            return  ResponseEntity.ok(new LoginResponse(jwt));
    }

    @PostMapping("/protected/auth/getToken")
    public ResponseEntity<?> getToken(Principal principal){
        try{
            String username = principal.getName();
            User user = userHandler.getUserByUsername(username);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            String jwt = jwtUtils.generateJwtToken(authentication,31536000000L);
            log.info("Token generated for CDI");
            return  ResponseEntity.ok(new LoginResponse(jwt));
        }catch(Exception e){
            log.warn("Error while generating token", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
