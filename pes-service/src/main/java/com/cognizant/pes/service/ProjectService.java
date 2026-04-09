package com.cognizant.pes.service;

import com.cognizant.pes.dao.ProjectDAOImpl;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.ProjectRequestDTO;
import com.cognizant.pes.dto.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectDAOImpl projectDAO;

    @Autowired  // <--- ADD THIS LINE HERE
    private ProjectMapper projectMapper;

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectDAO.findById(id);
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDTO saveProject(ProjectRequestDTO request) {
        // Without @Autowired above, projectMapper is null, causing your crash here
        Project project = projectMapper.toDomain(request);
        Project savedProject = projectDAO.saveProject(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectDAO.findAll();
        // Convert the List of Entities to a List of DTOs using the mapper
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        // Ensure the project exists before attempting deletion (optional but recommended)
        Project project = projectDAO.findById(id);
        if (project != null) {
            projectDAO.delete(id);
        }
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) throws ResourceNotFoundException{
        // 1. Fetch existing project
        Project existingProject = projectDAO.findById(id);

        if (existingProject != null) {
            // 2. Update fields from the request DTO
            existingProject.setTitle(request.title());
            existingProject.setBatchId(request.batchId());
            existingProject.setRepoUrl(request.repoUrl());

            // 3. Save the updated entity
            Project updatedProject = projectDAO.saveProject(existingProject);

            // 4. Return the mapped Response DTO
            return projectMapper.toDto(updatedProject);
        }

        throw new ResourceNotFoundException("unable to update the project"); // Or throw a Custom ResourceNotFoundException
    }
}
