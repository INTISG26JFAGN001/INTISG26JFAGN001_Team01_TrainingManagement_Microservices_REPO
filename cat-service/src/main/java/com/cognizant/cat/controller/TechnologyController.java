package com.cognizant.cat.controller;

import com.cognizant.cat.entity.Technology;
import com.cognizant.cat.service.ITechnologyService;
import com.cognizant.cat.dto.TechnologyRequestDTO;
import com.cognizant.cat.dto.TechnologyResponseDTO;
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

@Tag(name = "Technology Controller", description = "This controller handles all operations related to technology management. " +
        "It provides endpoints for creating, retrieving, updating, and deleting technologies. The controller interacts with " +
        "the technology service to manage technology data including technology names. Technologies are used as references for courses.")
@RestController
@RequestMapping("/technologies")
public class TechnologyController {

    private final ITechnologyService service;

    public TechnologyController(ITechnologyService service){
        this.service=service;
    }

    @Operation(summary = "Create Technology Endpoint",
            description = "This endpoint allows for the creation of new technologies. It accepts a JSON payload containing the " +
                    "technology name. The endpoint validates that the technology name is provided and is not blank. If the " +
                    "technology creation is successful, it returns the created technology details with HTTP status 201 (CREATED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Technology created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TechnologyResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Technology Creation",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Java\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation failed - technology name is blank or missing",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Missing Technology Name",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Technology name is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/technologies\"\n" +
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
                                                    "  \"path\": \"/technologies\"\n" +
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
                                                    "  \"path\": \"/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> create(
            @Valid @RequestBody TechnologyRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Retrieve All Technologies Endpoint",
            description = "This endpoint retrieves a list of all available technologies. It returns technology details including " +
                    "technology ID and name. The endpoint is useful for listing all technologies in the system for selection or " +
                    "display purposes when creating courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Technologies retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TechnologyResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Retrieval of All Technologies",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"name\": \"Java\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"name\": \"Python\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 3,\n" +
                                                    "    \"name\": \"Spring Boot\"\n" +
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
                                                    "  \"path\": \"/technologies\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Retrieve Technology by ID Endpoint",
            description = "This endpoint retrieves a specific technology by its unique identifier. It returns the technology details " +
                    "including technology ID and name. If the technology with the specified ID does not exist, an appropriate error " +
                    "response is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Technology retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TechnologyResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Technology Retrieval",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Java\"\n" +
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
                                                    "  \"path\": \"/technologies/999\"\n" +
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
                                                    "  \"path\": \"/technologies/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponseDTO> getById(
            @Parameter(description = "The unique identifier of the technology to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Update Technology Endpoint",
            description = "This endpoint allows for the update of an existing technology. It accepts a JSON payload containing the " +
                    "updated technology name. The endpoint validates that the technology name is provided and is not blank. If the " +
                    "update is successful, it returns the updated technology details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Technology updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TechnologyResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Technology Update",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"name\": \"Java 21\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation failed - technology name is blank or missing",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Missing Technology Name",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-17T10:00:00.0000000\",\n" +
                                                    "  \"message\": \"Technology name is required\",\n" +
                                                    "  \"errorCode\": \"V001\",\n" +
                                                    "  \"path\": \"/technologies/1\"\n" +
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
                                                    "  \"path\": \"/technologies/999\"\n" +
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
                                                    "  \"path\": \"/technologies/1\"\n" +
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
                                                    "  \"path\": \"/technologies/1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TechnologyResponseDTO> update(
            @Parameter(description = "The unique identifier of the technology to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TechnologyRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete Technology Endpoint",
            description = "This endpoint allows for the deletion of an existing technology. It removes the technology from the system. " +
                    "If the technology with the specified ID does not exist, an appropriate error response is returned. Upon successful " +
                    "deletion, a confirmation message is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Technology deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Technology Deletion",
                                            value = "\"Technology deleted successfully\""
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
                                                    "  \"path\": \"/technologies/999\"\n" +
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
                                                    "  \"path\": \"/technologies/999\"\n" +
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
                                                    "  \"path\": \"/technologies/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "The unique identifier of the technology to delete", example = "1")
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Technology deleted successfully");
    }
}
