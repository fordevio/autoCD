package com.fordevio.producer.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fordevio.producer.models.User;
import com.fordevio.producer.models.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String Username);
    List<User> findByRoles(Role role);
}
