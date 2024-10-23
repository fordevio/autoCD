package com.fordevio.producer.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fordevio.producer.models.Project;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
    
    Optional<Project> findByName(String name);
}
