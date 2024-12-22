package com.fordevio.producer.services.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fordevio.producer.models.Project;
import com.fordevio.producer.repositories.ProjectRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectHandlerImpl implements ProjectHandler {
    
    @Autowired
    private ProjectRepository projectRepository;

    @Override 
    public List<Project> getAllProjects() throws Exception{
        return projectRepository.findAll();
    }

    @Override
    public Project saveProject(Project project) throws Exception{
        return projectRepository.save(project);
    }

    @Override 
    public Project getProjectById(Long id) throws Exception{
        return projectRepository.findById(id).orElse(null);
    }

    @Override 
    public void deleteProject(Long id) throws Exception{
        projectRepository.deleteById(id);
    }

    @Override 
    public Project getProjectByName(String name) throws Exception{
        return projectRepository.findByName(name).orElse(null);
    }
}
