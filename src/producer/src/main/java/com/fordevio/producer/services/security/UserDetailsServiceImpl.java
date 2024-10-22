package com.fordevio.producer.services.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;
import com.fordevio.producer.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl  implements UserDetailsService {
        
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with username: "+ username));
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add((GrantedAuthority) () -> role.name());
        }
        for(Permission permission : user.getPermissions()){
            authorities.add((GrantedAuthority) () -> permission.name());
        }

        return new UserDetailsImpl(user.getUsername(),user.getId(),user.getPassword(), authorities);
    }
}
