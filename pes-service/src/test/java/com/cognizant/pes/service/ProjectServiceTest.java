package com.cognizant.pes.service;

import com.cognizant.pes.dao.impl.ProjectDAOImpl;
import com.cognizant.pes.domain.Project;
import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import com.cognizant.pes.mapper.ProjectMapper;
import com.cognizant.pes.service.impl.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectDAOImpl projectDAO;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectRequestDTO requestDTO;
    private ProjectResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setId(1L);
        project.setTitle("Demo Project");

        requestDTO = new ProjectRequestDTO("Demo Project", 101L, "http://repo.url");
        responseDTO = new ProjectResponseDTO(
                1L, 101L, "Demo Project", "http://repo.url", LocalDateTime.now()
        );
    }

    @Test
    void getProjectById_Found() {
        when(projectDAO.findById(1L)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(responseDTO);

        ProjectResponseDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Demo Project", result.title());
        verify(projectDAO).findById(1L);
        verify(projectMapper).toDto(project);
    }

    @Test
    void getProjectById_NotFound() {
        when(projectDAO.findById(99L)).thenReturn(null);

        ProjectResponseDTO result = projectService.getProjectById(99L);

        assertNull(result);
        verify(projectDAO).findById(99L);
        verify(projectMapper, never()).toDto(any());
    }

    @Test
    void saveProject_Success() {
        when(projectMapper.toDomain(requestDTO)).thenReturn(project);
        when(projectDAO.saveProject(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(responseDTO);

        ProjectResponseDTO result = projectService.saveProject(requestDTO);

        assertNotNull(result);
        assertEquals("Demo Project", result.title());
        verify(projectMapper).toDomain(requestDTO);
        verify(projectDAO).saveProject(project);
        verify(projectMapper).toDto(project);
    }

    @Test
    void getAllProjects_ReturnsList() {
        when(projectDAO.findAll()).thenReturn(Collections.singletonList(project));
        when(projectMapper.toDto(project)).thenReturn(responseDTO);

        List<ProjectResponseDTO> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        assertEquals("Demo Project", result.get(0).title());
        verify(projectDAO).findAll();
        verify(projectMapper).toDto(project);
    }

    @Test
    void deleteProject_Found() {
        when(projectDAO.findById(1L)).thenReturn(project);

        projectService.deleteProject(1L);

        verify(projectDAO).findById(1L);
        verify(projectDAO).delete(1L);
    }

    @Test
    void deleteProject_NotFound() {
        when(projectDAO.findById(99L)).thenReturn(null);

        projectService.deleteProject(99L);

        verify(projectDAO).findById(99L);
        verify(projectDAO, never()).delete(anyLong());
    }
}
