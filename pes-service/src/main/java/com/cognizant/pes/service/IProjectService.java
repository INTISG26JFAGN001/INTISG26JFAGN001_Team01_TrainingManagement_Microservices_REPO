package com.cognizant.pes.service;

import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;

import java.util.List;

public interface IProjectService {
    public ProjectResponseDTO saveProject(ProjectRequestDTO request);
    public ProjectResponseDTO getProjectById(Long id);
    public List<ProjectResponseDTO> getAllProjects();
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) throws ResourceNotFoundException;
    public void deleteProject(Long id);
}
