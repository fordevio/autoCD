package com.fordevio.producer.services;

import java.util.List;

import com.fordevio.producer.models.User;



public interface UserHandler {
    
    public void createAdminIfNot() throws Exception;
    public User getUserByUsername(String username) throws Exception;
    public User saveUser(User user) throws Exception;
    public List<User> getAllUsers() throws Exception;
    public User getUserById(Long id) throws Exception;
    public User getAdminUser() throws Exception;
    public void deleteUser(Long id) throws Exception;
}
