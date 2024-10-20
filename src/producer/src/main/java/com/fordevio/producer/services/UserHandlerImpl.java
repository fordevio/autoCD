package com.fordevio.producer.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;
import com.fordevio.producer.repositories.UserRepository;

@Service
public class UserHandlerImpl implements UserHandler{
 
    
    @Autowired 
    private UserRepository userRepository;

    public void createAdminIfNot(){
        List<User> admins = userRepository.findByRoles(Role.ADMIN);
        if(admins.isEmpty()){
            User admin = User.builder()
                    .username("admin")
                    .password("admin")
                    .roles(Set.of(Role.ADMIN, Role.MEMBER))
                    .permissions(Set.of(Permission.READ, Permission.WRITE, Permission.DELETE))
                    .build();
            userRepository.save(admin);
        }
    }
}
