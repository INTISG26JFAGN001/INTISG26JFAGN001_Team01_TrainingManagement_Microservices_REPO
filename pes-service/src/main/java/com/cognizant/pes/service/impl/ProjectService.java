package com.cognizant.pes.service.impl;

import com.cognizant.pes.dao.impl.ProjectDAOImpl;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.mapper.ProjectMapper;
import com.cognizant.pes.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectDAOImpl projectDAO;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectDAO.findById(id);
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDTO saveProject(ProjectRequestDTO request) {
        Project project = projectMapper.toDomain(request);
        Project savedProject = projectDAO.saveProject(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectDAO.findAll();
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectDAO.findById(id);
        if (project != null) {
            projectDAO.delete(id);
        }
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) throws ResourceNotFoundException{

        Project existingProject = projectDAO.findById(id);

        if (existingProject != null) {

            existingProject.setTitle(request.title());
            existingProject.setBatchId(request.batchId());
            existingProject.setRepoUrl(request.repoUrl());


            Project updatedProject = projectDAO.saveProject(existingProject);


            return projectMapper.toDto(updatedProject);
        }

        throw new ResourceNotFoundException("unable to update the project"); // Or throw a Custom ResourceNotFoundException
    }
}
