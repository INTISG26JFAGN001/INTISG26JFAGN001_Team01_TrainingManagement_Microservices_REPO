package com.cognizant.pes.dao.impl;

import com.cognizant.pes.dao.IProjectDAO;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectDAOImpl implements IProjectDAO {

    @Autowired
    private IProjectRepository projectRepository;


    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateProject(Long id, Project project) {
        if (projectRepository.existsById(id)) {
            project.setId(id);
            return projectRepository.save(project);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
