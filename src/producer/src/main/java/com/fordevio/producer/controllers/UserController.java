package com.fordevio.producer.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;
import com.fordevio.producer.payloads.requests.AddUpdateUserRequest;
import com.fordevio.producer.payloads.response.MessageResponse;
import com.fordevio.producer.services.UserHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/protected/user")
public class UserController {
    
   @Autowired
   private UserHandler userHandler;

   @GetMapping("/user")
   public ResponseEntity<?> getLoggedInUser(@AuthenticationPrincipal UserDetails userDetails){
       return ResponseEntity.ok(userDetails);
   }

   @PostMapping("/admin/add")
   public ResponseEntity<?> addUser(@Valid @RequestBody AddUpdateUserRequest user){
        try{
             User isAvail = userHandler.getUserByUsername(user.getUsername());
             if (isAvail != null){
                 return ResponseEntity.badRequest().body(new MessageResponse("User already exists"));
            }

            Set<Permission> permissions  = Set.of(Permission.READ);

            if(user.getPermissions() != null ) {
                    permissions = user.getPermissions();
            }

            User newUser = userHandler.saveUser(User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .permissions(permissions)
                    .roles(Set.of(Role.MEMBER))
                    .build());
            return ResponseEntity.ok(newUser);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
   }

   @GetMapping("/admin/all")
    public ResponseEntity<?> getAllUsers(){
         try{
              return ResponseEntity.ok(userHandler.getAllUsers());
         }catch(Exception e){
              return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
         }
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
       try{
               User user = userHandler.getUserById(id);
               if(user == null){
                    return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
               }
               User adminUser = userHandler.getAdminUser();

               if (adminUser!=null && adminUser.getId().equals(id)){
                   return ResponseEntity.badRequest().body(new MessageResponse("Admin user cannot be deleted"));
               }
               userHandler.deleteUser(id);
               return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
       }catch(Exception e){
           return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
       }
          
     }

     @PutMapping("/admin/update/{id}")
     public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody AddUpdateUserRequest user){
         try{
             User userToUpdate = userHandler.getUserById(id);
             if(userToUpdate == null){
                 return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
             }
             User adminUser = userHandler.getAdminUser();
             Set<Permission> permissions = userToUpdate.getPermissions();
             if (adminUser!= null&& user.getPermissions() != null && !adminUser.getId().equals(id)){
                 permissions = user.getPermissions();
             }
             userToUpdate.setUsername(user.getUsername());
             userToUpdate.setPassword(user.getPassword());
             userToUpdate.setPermissions(permissions);
             userHandler.saveUser(userToUpdate);
             return ResponseEntity.ok(userToUpdate);
         }catch(Exception e){
             return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
         }
     }

}
