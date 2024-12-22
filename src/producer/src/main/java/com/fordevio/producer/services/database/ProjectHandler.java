package com.fordevio.producer.services.database;

import java.util.List;

import com.fordevio.producer.models.Project;;

public interface ProjectHandler {
    public List<Project> getAllProjects() throws Exception;
    public Project saveProject(Project project) throws Exception;
    public Project getProjectById(Long id) throws Exception;
    public void deleteProject(Long id) throws Exception;
    public Project getProjectByName(String name) throws Exception;
}
