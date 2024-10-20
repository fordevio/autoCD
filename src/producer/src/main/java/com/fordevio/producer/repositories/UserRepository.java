package com.fordevio.producer.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fordevio.producer.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String Username);
    Optional<User> findById(Long id);
    List<User> findByIsAdminTrue();
}
