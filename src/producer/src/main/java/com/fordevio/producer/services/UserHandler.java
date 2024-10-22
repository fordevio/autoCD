package com.fordevio.producer.services;

import com.fordevio.producer.models.User;


public interface UserHandler {
    
    public void createAdminIfNot() throws Exception;
    public User getUserByUsername(String username) throws Exception;
    public User saveUser(User user) throws Exception;
}
