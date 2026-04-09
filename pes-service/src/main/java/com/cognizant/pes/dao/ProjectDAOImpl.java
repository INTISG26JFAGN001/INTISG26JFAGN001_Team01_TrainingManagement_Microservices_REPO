package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Project;
import com.cognizant.pes.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDAOImpl implements IProjectDAO{

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
        // In JPA, save() performs an update if the entity has an existing ID
        if (projectRepository.existsById(id)) {
            project.setId(id); // Ensure the ID matches the path variable
            return projectRepository.save(project);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
