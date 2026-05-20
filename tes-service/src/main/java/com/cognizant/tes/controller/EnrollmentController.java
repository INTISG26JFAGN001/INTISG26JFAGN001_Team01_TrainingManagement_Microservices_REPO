package com.cognizant.tes.controller;

import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.dto.ErrorResponseDTO;
import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;
import com.cognizant.tes.mapper.EnrollmentMapper;
import com.cognizant.tes.service.IEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollment Management", description = "Endpoints for creating and managing enrollments of associates into batches")
@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {


    private final IEnrollmentService enrollmentService;

    public EnrollmentController(IEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Create Enrollment",
            description = "Creates a new enrollment for an associate in a batch. " +
                    "The request must include both batchId and associateId. " +
                    "If successful, the enrollment details are returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollment created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"enrollmentId\": 1001,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"associateId\": 501,\n" +
                                                    "  \"status\": \"ENROLLED\",\n" +
                                                    "  \"joinDate\": \"2026-04-16\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:42:00\",\n" +
                                                    "  \"message\": \"Failed to create enrollment\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    private ResponseEntity<EnrollmentDTO> createEnrollment(@Valid @RequestBody EnrollmentDTO dto){
        Enrollment created = enrollmentService.createEnrollment(dto);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(created));
    }

    @Operation(summary = "Get Enrollment by ID",
            description = "Retrieves enrollment details for a specific enrollment ID. " +
                    "If the enrollment exists, its details are returned. " +
                    "If the enrollment does not exist, a 404 error is returned. " +
                    "If the provided ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollment retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"enrollmentId\": 1001,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"associateId\": 501,\n" +
                                                    "  \"status\": \"ENROLLED\",\n" +
                                                    "  \"joinDate\": \"2026-04-16\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid enrollment ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:45:00\",\n" +
                                                    "  \"message\": \"Enrollment ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/enrollment/-1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:46:00\",\n" +
                                                    "  \"message\": \"Enrollment with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/enrollment/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:47:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve enrollment\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/1001\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    private ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id){
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(enrollment));
    }

    @Operation(summary = "Get All Enrollments",
            description = "Retrieves all enrollments in the system. " +
                    "If enrollments exist, they are returned as a list of EnrollmentDTOs. " +
                    "If no enrollments exist, the response is still 200 with an empty list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1001,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"associateId\": 501,\n" +
                                                    "    \"status\": \"ENROLLED\",\n" +
                                                    "    \"joinDate\": \"2026-04-16\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1002,\n" +
                                                    "    \"batchId\": 102,\n" +
                                                    "    \"associateId\": 502,\n" +
                                                    "    \"status\": \"PENDING\",\n" +
                                                    "    \"joinDate\": \"2026-04-17\"\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:50:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve enrollments\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping
    private ResponseEntity<List<EnrollmentDTO>> getAllEnrollments(){
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get Enrollments by Batch ID",
            description = "Retrieves all enrollments associated with a given batch ID. " +
                    "If enrollments exist, they are returned as a list of EnrollmentDTOs. " +
                    "If no enrollments exist, the response is still 200 with an empty list. " +
                    "If the batchId is invalid (e.g., null or negative), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1001,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"associateId\": 501,\n" +
                                                    "    \"status\": \"ENROLLED\",\n" +
                                                    "    \"joinDate\": \"2026-04-16\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1002,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"associateId\": 502,\n" +
                                                    "    \"status\": \"PENDING\",\n" +
                                                    "    \"joinDate\": \"2026-04-17\"\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid batch ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid Batch ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:55:00\",\n" +
                                                    "  \"message\": \"Batch ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/enrollment/batch?id=-1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T16:56:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve enrollments by batchId\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/batch?id=101\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/batch")
    private ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByBatch(@RequestParam("id") Long batchId){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByBatchId(batchId);
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get Enrollment by Associate ID",
            description = "Retrieves the enrollment details for a specific associate ID. " +
                    "If the enrollment exists, its details are returned. " +
                    "If the enrollment does not exist, a 404 error is returned. " +
                    "If the provided associateId is invalid (e.g., null or negative), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollment retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"enrollmentId\": 1001,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"associateId\": 501,\n" +
                                                    "  \"status\": \"ENROLLED\",\n" +
                                                    "  \"joinDate\": \"2026-04-16\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid associate ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid Associate ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:58:00\",\n" +
                                                    "  \"message\": \"Associate ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/enrollment/associate?id=-1\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrollment not found for associate ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:59:00\",\n" +
                                                    "  \"message\": \"Enrollment for associateId=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/enrollment/associate?id=999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:00:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve enrollment by associateId\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/associate?id=501\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/associate")
    private ResponseEntity<EnrollmentDTO> getEnrollmentByAssociate(@RequestParam("id") Long associateId){
        Enrollment enrollment = enrollmentService.getEnrollmentByAssociateId(associateId);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(enrollment));
    }

    @Operation(summary = "Update Enrollment Status",
            description = "Updates the status of an existing enrollment by its ID. " +
                    "If the enrollment exists, its status is updated and the updated enrollment details are returned. " +
                    "If the enrollment does not exist, a 404 error is returned. " +
                    "If the provided ID or status value is invalid, a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollment status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"enrollmentId\": 1001,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"associateId\": 501,\n" +
                                                    "  \"status\": \"COMPLETED\",\n" +
                                                    "  \"joinDate\": \"2026-04-16\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:07:00\",\n" +
                                                    "  \"message\": \"Enrollment with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/enrollment/999/status?val=COMPLETED\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:08:00\",\n" +
                                                    "  \"message\": \"Failed to update enrollment status\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/1001/status?val=COMPLETED\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{id}/status")
    private ResponseEntity<EnrollmentDTO> updateStatus(@PathVariable("id") Long id,
                                                       @RequestParam("val") EnrollmentStatus status){
        Enrollment updated = enrollmentService.updateStatus(id, status);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(updated));
    }

    @Operation(summary = "Get Enrollments by Status",
            description = "Retrieves all enrollments that match the given status. " +
                    "If enrollments exist, they are returned as a list of EnrollmentDTOs. " +
                    "If no enrollments match, the response is still 200 with an empty list. " +
                    "If the status value is invalid (not part of the EnrollmentStatus enum), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response with Data",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1001,\n" +
                                                    "    \"batchId\": 101,\n" +
                                                    "    \"associateId\": 501,\n" +
                                                    "    \"status\": \"ENROLLED\",\n" +
                                                    "    \"joinDate\": \"2026-04-16\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"enrollmentId\": 1002,\n" +
                                                    "    \"batchId\": 102,\n" +
                                                    "    \"associateId\": 502,\n" +
                                                    "    \"status\": \"PENDING\",\n" +
                                                    "    \"joinDate\": \"2026-04-17\"\n" +
                                                    "  }\n" +
                                                    "]"
                                    ),
                                    @ExampleObject(
                                            name = "Successful Response with Empty List",
                                            value = "[]"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:11:00\",\n" +
                                                    "  \"message\": \"Failed to retrieve enrollments by status\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/status?val=ENROLLED\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/status")
    private ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStatus(@RequestParam("val") EnrollmentStatus status){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        List<EnrollmentDTO> dtos = enrollments.stream().map(EnrollmentMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Delete Enrollment",
            description = "Deletes an enrollment by its ID. " +
                    "If the enrollment exists, it is deleted and the deleted enrollment details are returned. " +
                    "If the enrollment does not exist, a 404 error is returned. " +
                    "If the provided ID is invalid (e.g., less than 1), a 400 error is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Enrollment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnrollmentDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Successful Response",
                                            value = "{\n" +
                                                    "  \"enrollmentId\": 1001,\n" +
                                                    "  \"batchId\": 101,\n" +
                                                    "  \"associateId\": 501,\n" +
                                                    "  \"status\": \"ENROLLED\",\n" +
                                                    "  \"joinDate\": \"2026-04-16\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid enrollment ID provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bad Request - Invalid ID",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:15:00\",\n" +
                                                    "  \"message\": \"Enrollment ID must be positive\",\n" +
                                                    "  \"errorCode\": \"TES-400\",\n" +
                                                    "  \"path\": \"/enrollment/0\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrollment not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Not Found Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T17:16:00\",\n" +
                                                    "  \"message\": \"Enrollment with id=999 not found\",\n" +
                                                    "  \"errorCode\": \"TES-404\",\n" +
                                                    "  \"path\": \"/enrollment/999\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - user not authorized to create schedule",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T16:40:00\",\n" +
                                                    "  \"message\": \"You are not authorized to create schedules\",\n" +
                                                    "  \"errorCode\": \"TES-403\",\n" +
                                                    "  \"path\": \"/schedule\"\n" +
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
                                                    "  \"timestamp\": \"2026-04-16T17:17:00\",\n" +
                                                    "  \"message\": \"Failed to delete enrollment\",\n" +
                                                    "  \"errorCode\": \"TES-500\",\n" +
                                                    "  \"path\": \"/enrollment/1001\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<EnrollmentDTO> deleteEnrollment(@PathVariable Long id){
        Enrollment deleted = enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(EnrollmentMapper.toDTO(deleted));
    }
}
