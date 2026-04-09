package com.cognizant.pes.service;

import com.cognizant.pes.dto.ProjectRequestDTO;
import com.cognizant.pes.dto.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;

import java.util.List;

public interface IProjectService {
    public ProjectResponseDTO saveProject(ProjectRequestDTO request);
    public ProjectResponseDTO getProjectById(Long id);
    public List<ProjectResponseDTO> getAllProjects();
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) throws ResourceNotFoundException;
    public void deleteProject(Long id);
}
