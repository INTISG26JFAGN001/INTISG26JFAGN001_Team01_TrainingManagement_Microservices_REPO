package com.cognizant.pes.controller;

import com.cognizant.pes.dto.ProjectRequestDTO;
import com.cognizant.pes.dto.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // 1. Create a new Project
    @PostMapping("/submitProject")
    public ResponseEntity<ProjectResponseDTO> submitProject(@Valid @RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = projectService.saveProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get a single Project by ID
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable Long projectId) {
        ProjectResponseDTO response = projectService.getProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    // 3. Get all Projects (Useful for Instructors/Admins)
    @GetMapping("/getProjects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // 4. Update an existing Project (e.g., correcting a Repo URL)
    @PutMapping("/update/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDTO request) throws ResourceNotFoundException {
        ProjectResponseDTO response = projectService.updateProject(projectId, request);
        return ResponseEntity.ok(response);
    }

    // 5. Delete a Project
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
}