package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Project;

import java.util.List;

public interface IProjectDAO {
    public Project saveProject(Project project);
    public Project findById(Long id);
    public List<Project> findAll();
    public Project updateProject(Long id, Project project);
    public void delete(Long id);
}
