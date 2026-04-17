package com.cognizant.cat.controller;

import com.cognizant.cat.dto.CourseRequestDTO;
import com.cognizant.cat.dto.CourseResponseDTO;
import com.cognizant.cat.dto.ErrorResponseDTO;
import com.cognizant.cat.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Course Controller", description = "This controller handles all operations related to course management. " +
        "It provides endpoints for creating, retrieving, updating, and deleting courses. The controller interacts with " +
        "the course service to manage course data including course code, title, technology, and duration.")
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService service;

    public CourseController(ICourseService service){
        this.service=service;
    }

    @Operation(summary = "Create Course Endpoint",
            description = "This endpoint allows for the creation of new courses. It accepts a JSON payload containing the " +
                    "course code, title, technology ID, and duration in days. The endpoint validates that all required fields " +
                    "are provided and that the referenced technology exists. If the course creation is successful, it returns " +
                    "the created course details with HTTP status 201 (CREATED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Course created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Course Creation",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"code\": \"JAVA101\",\n" +
                                                    "  \"title\": \"Introduction to Java\",\n" +
                                                    "  \"technologyName\": \"Java\",\n" +
                                                    "  \"durationDays\": 30\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation failed - one or more required fields are blank or missing",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Missing Code",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Code is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Missing Title",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Title is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Missing Duration",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Duration is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Technology not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Technology Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Technology not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Not authorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Authorized",
                                            value = ""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Something went wrong\",\n" +
                                                    "  \"errorCode\": \"S500\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@Valid @RequestBody CourseRequestDTO dto) {

        CourseResponseDTO response=service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Retrieve All Courses Endpoint",
            description = "This endpoint retrieves a list of all available courses. It returns course details including " +
                    "course ID, code, title, technology name, and duration. The endpoint is useful for listing all courses " +
                    "in the system for selection or display purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Courses retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Retrieval of All Courses",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"code\": \"JAVA101\",\n" +
                                                    "    \"title\": \"Introduction to Java\",\n" +
                                                    "    \"technologyName\": \"Java\",\n" +
                                                    "    \"durationDays\": 30\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"code\": \"PYTHON101\",\n" +
                                                    "    \"title\": \"Introduction to Python\",\n" +
                                                    "    \"technologyName\": \"Python\",\n" +
                                                    "    \"durationDays\": 25\n" +
                                                    "  }\n" +
                                                    "]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Not authorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Authorized",
                                            value = ""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Something went wrong\",\n" +
                                                    "  \"errorCode\": \"S500\",\n" +
                                                    "  \"path\": \"/courses\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAll() {

        List<CourseResponseDTO> list=service.getAll();
        System.out.println(list);
        return ResponseEntity.ok(service.getAll());

    }

    @Operation(summary = "Retrieve Course by ID Endpoint",
            description = "This endpoint retrieves a specific course by its unique identifier. It returns the course details " +
                    "including course ID, code, title, technology name, and duration. If the course with the specified ID does " +
                    "not exist, an appropriate error response is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Course retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Course Retrieval",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"code\": \"JAVA101\",\n" +
                                                    "  \"title\": \"Introduction to Java\",\n" +
                                                    "  \"technologyName\": \"Java\",\n" +
                                                    "  \"durationDays\": 30\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Course not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Course Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Course not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Not authorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Authorized",
                                            value = ""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Something went wrong\",\n" +
                                                    "  \"errorCode\": \"S500\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getById(
            @Parameter(description = "The unique identifier of the course to retrieve", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Update Course Endpoint",
            description = "This endpoint allows for the update of an existing course. It accepts a JSON payload containing the " +
                    "updated course code, title, technology ID, and duration. The endpoint validates that all required fields " +
                    "are provided and that the referenced technology exists. If the update is successful, it returns the updated " +
                    "course details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Course updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Course Update",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"code\": \"JAVA102\",\n" +
                                                    "  \"title\": \"Advanced Java Programming\",\n" +
                                                    "  \"technologyName\": \"Java\",\n" +
                                                    "  \"durationDays\": 40\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Course or technology not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Course Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Course not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Technology Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Technology not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/courses/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Not authorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Authorized",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Access denied. Insufficient permissions to perform this action.\",\n" +
                                                    "  \"errorCode\": \"A403\",\n" +
                                                    "  \"path\": \"/courses/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Something went wrong\",\n" +
                                                    "  \"errorCode\": \"S500\",\n" +
                                                    "  \"path\": \"/courses/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(
            @Parameter(description = "The unique identifier of the course to update", example = "1")
            @PathVariable Long id, 
            @RequestBody CourseRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete Course Endpoint",
            description = "This endpoint allows for the deletion of an existing course. It removes the course and all " +
                    "associated data from the system. If the course with the specified ID does not exist, an appropriate " +
                    "error response is returned. Upon successful deletion, a confirmation message is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Course deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Course Deletion",
                                            value = "\"Course deleted successfully\""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Course not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Course Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Course not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Not authorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Authorized",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Access denied. Insufficient permissions to perform this action.\",\n" +
                                                    "  \"errorCode\": \"A403\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Something went wrong\",\n" +
                                                    "  \"errorCode\": \"S500\",\n" +
                                                    "  \"path\": \"/courses/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "The unique identifier of the course to delete", example = "1")
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Course deleted successfully");
    }

}
