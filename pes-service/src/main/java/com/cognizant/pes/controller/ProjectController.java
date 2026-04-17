package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.ProjectRequestDTO;
import com.cognizant.pes.dto.response.ProjectResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.service.impl.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(
        name = "Project Controller",
        description = "This controller handles all operations related to project submissions and management within PES. " +
                "It allows associates or trainers to submit project details, retrieve projects, update existing projects, " +
                "and delete projects linked to batches."
)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(
            summary = "Submit Project",
            description = "This endpoint allows submission of a new project linked to a specific batch. " +
                    "The request includes the project title and repository URL. Upon successful submission, " +
                    "the persisted project details are returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Project submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Project Created",
                                    value = "{\n" +
                                            "  \"title\": \"Employee Management System\",\n" +
                                            "  \"batchId\": 25,\n" +
                                            "  \"repoUrl\": \"https://github.com/org/ems-project\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid project input",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"timestamp\": \"2026-04-17T10:10:00\",\n" +
                                            "  \"message\": \"Must be a valid URL\",\n" +
                                            "  \"errorCode\": \"P001\",\n" +
                                            "  \"path\": \"/projects/submitProject\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/submitProject")
    public ResponseEntity<ProjectResponseDTO> submitProject(
            @Valid @RequestBody ProjectRequestDTO request) {

        ProjectResponseDTO response = projectService.saveProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get Project by ID",
            description = "Fetches a single project using its unique project ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"timestamp\": \"2026-04-17T10:15:00\",\n" +
                                            "  \"message\": \"Project with ID 201 not found\",\n" +
                                            "  \"errorCode\": \"P404\",\n" +
                                            "  \"path\": \"/projects/201\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(
            @PathVariable Long projectId) {

        ProjectResponseDTO response = projectService.getProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Projects",
            description = "Retrieves all projects available in the system across batches."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Projects retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDTO.class)
                    )
            )
    })
    @GetMapping("/getProjects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {

        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(
            summary = "Update Project",
            description = "Updates an existing project identified by project ID. " +
                    "The request body contains updated project details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project not found"
            )
    })
    @PutMapping("/update/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDTO request)
            throws ResourceNotFoundException {

        ProjectResponseDTO response =
                projectService.updateProject(projectId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Project",
            description = "Deletes an existing project identified by project ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId) {

        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}