package com.fordevio.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.User;
import com.fordevio.producer.payloads.requests.AddUpdateUserRequest;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.UserHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/protected")
public class UserController {
    
   @Autowired
   private UserHandler userHandler;

   @GetMapping("/user")
   public ResponseEntity<?> getLoggedInUser(@AuthenticationPrincipal UserDetails userDetails){
       return ResponseEntity.ok(userDetails);
   }

   @PostMapping("/admin/user/add")
   public ResponseEntity<?> addUser(@Valid @RequestBody AddUpdateUserRequest user){
        try{
             User isAvail = userHandler.getUserByUsername(user.getUsername());
             if (isAvail != null){
                 return ResponseEntity.badRequest().body(new MessageResponse("User already exists"));
            }
            User newUser = userHandler.saveUser(User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .permissions(user.getPermissions())
                    .roles(user.getRoles())
                    .build());
            return ResponseEntity.ok(newUser);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
   }
}
