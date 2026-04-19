package com.cognizant.pes.service.impl;

import com.cognizant.pes.dao.impl.ProjectDAOImpl;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.mapper.ProjectMapper;
import com.cognizant.pes.service.IProjectService;
import lombok.extern.slf4j.Slf4j;   // ✅ Lombok logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectDAOImpl projectDAO;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        log.info("Fetching project with id={}", id);
        Project project = projectDAO.findById(id);
        if (project == null) {
            log.warn("Project with id={} not found", id);
            return null;
        }
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDTO saveProject(ProjectRequestDTO request) {
        log.info("Saving new project with title={} and batchId={}", request.title(), request.batchId());
        Project project = projectMapper.toDomain(request);
        Project savedProject = projectDAO.saveProject(project);
        log.info("Project saved successfully with id={}", savedProject.getId());
        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        log.info("Fetching all projects");
        List<Project> projects = projectDAO.findAll();
        log.debug("Found {} projects", projects.size());
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        log.info("Deleting project with id={}", id);
        Project project = projectDAO.findById(id);
        if (project != null) {
            projectDAO.delete(id);
            log.info("Project with id={} deleted successfully", id);
        } else {
            log.warn("Project with id={} not found, cannot delete", id);
        }
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) throws ResourceNotFoundException {
        log.info("Updating project with id={}", id);
        Project existingProject = projectDAO.findById(id);

        if (existingProject != null) {
            existingProject.setTitle(request.title());
            existingProject.setBatchId(request.batchId());
            existingProject.setRepoUrl(request.repoUrl());

            Project updatedProject = projectDAO.saveProject(existingProject);
            log.info("Project with id={} updated successfully", updatedProject.getId());
            return projectMapper.toDto(updatedProject);
        }

        log.error("Unable to update project with id={} because it was not found", id);
        throw new ResourceNotFoundException("Unable to update the project");
    }
}
