package com.cognizant.cat.controller;

import com.cognizant.cat.entity.Stage;
import com.cognizant.cat.service.IStageService;
import com.cognizant.cat.dto.StageRequestDTO;
import com.cognizant.cat.dto.StageResponseDTO;
import com.cognizant.cat.dto.ErrorResponseDTO;
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

@Tag(name = "Stage Controller", description = "This controller handles all operations related to stage management. " +
        "It provides endpoints for creating, retrieving, updating, and deleting training stages. The controller interacts with " +
        "the stage service to manage stage data including stage name, order, type, and associated course. Stages represent " +
        "different learning phases within a course.")
@RestController
@RequestMapping("/stages")
public class StageController {

    private final IStageService service;

    public StageController(IStageService service) {
        this.service=service;
    }

    @Operation(summary = "Create Stage Endpoint",
            description = "This endpoint allows for the creation of new training stages within a course. It accepts a JSON payload " +
                    "containing the stage name, order (sequence), type, and course ID. The endpoint validates that all required fields " +
                    "are provided and that the referenced course exists. If the stage creation is successful, it returns the created " +
                    "stage details with HTTP status 201 (CREATED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Stage created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StageResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Stage Creation",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Introduction\",\n" +
                                                    "  \"ord\": 1,\n" +
                                                    "  \"type\": \"Theoretical\",\n" +
                                                    "  \"courseTitle\": \"Introduction to Java\"\n" +
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
                                            name = "Missing Stage Name",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Stage name is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/stages\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Missing Stage Type",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Type is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/stages\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Missing Stage Order",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Order is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/stages\"\n" +
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
                                                    "  \"path\": \"/stages\"\n" +
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
                                                    "  \"path\": \"/stages\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<StageResponseDTO> create(
            @Valid @RequestBody StageRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Retrieve All Stages Endpoint",
            description = "This endpoint retrieves a list of all available training stages. It returns stage details including " +
                    "stage ID, name, order, type, and associated course title. The endpoint is useful for listing all stages in " +
                    "the system for selection or display purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stages retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StageResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Retrieval of All Stages",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"name\": \"Introduction\",\n" +
                                                    "    \"ord\": 1,\n" +
                                                    "    \"type\": \"Theoretical\",\n" +
                                                    "    \"courseTitle\": \"Introduction to Java\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"name\": \"Practical\",\n" +
                                                    "    \"ord\": 2,\n" +
                                                    "    \"type\": \"Hands-on\",\n" +
                                                    "    \"courseTitle\": \"Introduction to Java\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 3,\n" +
                                                    "    \"name\": \"Assessment\",\n" +
                                                    "    \"ord\": 3,\n" +
                                                    "    \"type\": \"Evaluation\",\n" +
                                                    "    \"courseTitle\": \"Introduction to Java\"\n" +
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
                                                    "  \"path\": \"/stages\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<StageResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Retrieve Stage by ID Endpoint",
            description = "This endpoint retrieves a specific training stage by its unique identifier. It returns the stage details " +
                    "including stage ID, name, order, type, and associated course title. If the stage with the specified ID does not " +
                    "exist, an appropriate error response is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stage retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StageResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Stage Retrieval",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Introduction\",\n" +
                                                    "  \"ord\": 1,\n" +
                                                    "  \"type\": \"Theoretical\",\n" +
                                                    "  \"courseTitle\": \"Introduction to Java\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Stage not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Stage Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Stage not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/stages/999\"\n" +
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
                                                    "  \"path\": \"/stages/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<StageResponseDTO> getById(
            @Parameter(description = "The unique identifier of the stage to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Update Stage Endpoint",
            description = "This endpoint allows for the update of an existing training stage. It accepts a JSON payload containing the " +
                    "updated stage name, order, type, and course ID. The endpoint validates that all required fields are provided and " +
                    "that the referenced course exists. If the update is successful, it returns the updated stage details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stage updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StageResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Stage Update",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Basics\",\n" +
                                                    "  \"ord\": 1,\n" +
                                                    "  \"type\": \"Fundamentals\",\n" +
                                                    "  \"courseTitle\": \"Introduction to Java\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation failed - invalid or missing values",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid Stage Order",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Order is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/stages/1\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Stage Type",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Type is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/stages/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Stage or course not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Stage Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Stage not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/stages/999\"\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "Course Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Course not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/stages/1\"\n" +
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
                                                    "  \"path\": \"/stages/1\"\n" +
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
                                                    "  \"path\": \"/stages/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<StageResponseDTO> update(
            @Parameter(description = "The unique identifier of the stage to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody StageRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete Stage Endpoint",
            description = "This endpoint allows for the deletion of an existing training stage. It removes the stage and all " +
                    "associated data from the system. If the stage with the specified ID does not exist, an appropriate error response " +
                    "is returned. Upon successful deletion, a confirmation message is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stage deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Stage Deletion",
                                            value = "\"Stage deleted successfully\""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Stage not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Stage Not Found",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Stage not found\",\n" +
                                                    "  \"errorCode\": \"R001\",\n" +
                                                    "  \"path\": \"/stages/999\"\n" +
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
                                                    "  \"path\": \"/stages/999\"\n" +
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
                                                    "  \"path\": \"/stages/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "The unique identifier of the stage to delete", example = "1")
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Stage deleted successfully");
    }
}
