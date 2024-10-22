package com.fordevio.producer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class UserController {
    
   @GetMapping("/user")
   public ResponseEntity<?> getLoggedInUser(@AuthenticationPrincipal UserDetails userDetails){
       return ResponseEntity.ok(userDetails);
   }
   
}
