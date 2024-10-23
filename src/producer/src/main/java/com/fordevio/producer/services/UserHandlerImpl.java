package com.fordevio.producer.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;
import com.fordevio.producer.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserHandlerImpl implements UserHandler{
 
    
    @Autowired 
    private UserRepository userRepository;

    @Override
    public void createAdminIfNot() throws Exception{
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

    @Override
    public User getUserByUsername(String username) throws Exception{
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override 
    @Transactional
    public User saveUser(User user) throws Exception{
        return userRepository.save(user);
    }

    @Override 
    public List<User> getAllUsers() throws Exception{
        return userRepository.findAll();
    }

    @Override 
    public User getUserById(Long id) throws Exception{
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getAdminUser() throws Exception{
        return userRepository.findByRoles(Role.ADMIN).stream().findFirst().orElse(null);
    }

    @Override 
    public void deleteUser(Long id) throws Exception{
        userRepository.deleteById(id);
    }
}
