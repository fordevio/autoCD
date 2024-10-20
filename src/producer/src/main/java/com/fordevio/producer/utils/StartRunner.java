package com.fordevio.producer.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartRunner {
    
    @Autowired
    private  UserRepository userRepository;

    @PostConstruct
    public void createAdminIfNot(){
        List<User> admins = userRepository.findByIsAdminTrue();
        if(admins.isEmpty()){
            User admin = User.builder()
                    .username("admin")
                    .password("admin")
                    .isAdmin(true)
                    .permission(Permission.DELETE)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created");
        }
    }
}
