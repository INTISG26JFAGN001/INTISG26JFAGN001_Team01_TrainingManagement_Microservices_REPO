package com.cognizant.tes.controller;

import com.cognizant.tes.client.ICourseServiceClient;
import com.cognizant.tes.dao.ICourseBatchMapDAO;
import com.cognizant.tes.dto.BatchDTO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.dto.CourseResponseDTO;
import com.cognizant.tes.dto.ErrorResponseDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.mapper.BatchMapper;
import com.cognizant.tes.service.IBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

    @Tag(name = "Batch Management", description = "Endpoints for creating and managing training batches")
    @RestController
    @RequestMapping("/batches")
    public class BatchController {

        private final IBatchService batchService;
        private final ICourseBatchMapDAO courseBatchMapDAO;
        private final ICourseServiceClient courseServiceClient;

        public BatchController(IBatchService batchService, ICourseBatchMapDAO courseBatchMapDAO,
                               ICourseServiceClient courseServiceClient) {
            this.courseServiceClient = courseServiceClient;
            this.batchService = batchService;
            this.courseBatchMapDAO = courseBatchMapDAO;
        }

        @Operation(summary = "Create a new batch",
                description = "Creates a new training batch. The request must include a valid trainerId and at least one courseId. " +
                        "BatchId is generated automatically. Course names are resolved from the provided courseIds.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201",
                        description = "Batch created successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDetailsDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response",
                                                value = "{\n" +
                                                        "  \"id\": 101,\n" +
                                                        "  \"trainerId\": 501,\n" +
                                                        "  \"status\": \"ACTIVE\",\n" +
                                                        "  \"startDate\": \"2026-04-16T09:00:00\",\n" +
                                                        "  \"endDate\": \"2026-06-16T17:00:00\",\n" +
                                                        "  \"courseIds\": [201, 202],\n" +
                                                        "  \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T15:56:00\",\n" +
                                                        "  \"message\": \"Failed to create batch\",\n" +
                                                        "  \"errorCode\": \"B500\",\n" +
                                                        "  \"path\": \"/batches\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @PostMapping
        public ResponseEntity<BatchDetailsDTO> createBatch(@RequestBody BatchDetailsDTO requestDto) {

            Batch savedBatch = batchService.addBatch(requestDto);


            BatchDetailsDTO responseDto = new BatchDetailsDTO();
            responseDto.setId(savedBatch.getBatchId());
            responseDto.setTrainerId(savedBatch.getTrainerId());
            responseDto.setStatus(savedBatch.getStatus());

            List<String> courseNames = requestDto.getCourseIds().stream()
                    .map(courseServiceClient::getCourseById)
                    .map(CourseResponseDTO::getTitle)
                    .collect(Collectors.toList());

            responseDto.setCourseNames(courseNames);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }

        @Operation(summary = "Get All Batches",
                description = "Retrieves a list of all training batches. Each batch includes its trainer, status, and associated course names. " +
                        "If no batches exist, the response is still 200 with an empty list.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "List of batches retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response with Data",
                                                value = "[\n" +
                                                        "  {\n" +
                                                        "    \"id\": 101,\n" +
                                                        "    \"trainerId\": 501,\n" +
                                                        "    \"status\": \"ACTIVE\",\n" +
                                                        "    \"courseIds\": [201, 202],\n" +
                                                        "    \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "  },\n" +
                                                        "  {\n" +
                                                        "    \"id\": 102,\n" +
                                                        "    \"trainerId\": 502,\n" +
                                                        "    \"status\": \"PLANNED\",\n" +
                                                        "    \"courseIds\": [203],\n" +
                                                        "    \"courseNames\": [\"Python Fundamentals\"]\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:00:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batches\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping
        public ResponseEntity<List<BatchDTO>> getAllBatches() {
            List<BatchDTO> batchDTOs = batchService.getAllBatches().stream()
                    .map(batch -> {
                        List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batch.getBatchId());
                        List<String> courseNames = courseIds.stream()
                                .map(courseServiceClient::getCourseById)
                                .map(CourseResponseDTO::getTitle)
                                .collect(Collectors.toList());

                        BatchDTO dto = new BatchDTO();
                        dto.setId(batch.getBatchId());
                        dto.setTrainerId(batch.getTrainerId());
                        dto.setStatus(batch.getStatus());
                        dto.setCourseNames(courseNames);
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(batchDTOs);
        }


        @Operation(
                summary = "Get Batch by ID",
                description = "Retrieves an existing batch by its ID. " +
                        "If the batch exists, its details along with associated course names are returned. " +
                        "If the batch does not exist, a 404 error is returned. " +
                        "If the ID is negative, a 400 error is returned."
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batch retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response",
                                                value = "{\n" +
                                                        "  \"id\": 101,\n" +
                                                        "  \"trainerId\": 501,\n" +
                                                        "  \"status\": \"ACTIVE\",\n" +
                                                        "  \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "400",
                        description = "Invalid ID (negative value)",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Bad Request Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-17T09:55:00\",\n" +
                                                        "  \"message\": \"Batch ID must be non-negative\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/-5\"\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "404",
                        description = "Batch not found",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Not Found Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-17T09:56:00\",\n" +
                                                        "  \"message\": \"Batch with id=999 not found\",\n" +
                                                        "  \"errorCode\": \"TES-404\",\n" +
                                                        "  \"path\": \"/batches/999\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-17T09:57:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batch\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/101\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/{id}")
        public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
            Batch batch = batchService.getBatchById(id);

            List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batch.getBatchId());
            List<String> courseNames = courseIds.stream()
                    .map(courseServiceClient::getCourseById)
                    .map(CourseResponseDTO::getTitle)
                    .collect(Collectors.toList());

            BatchDTO dto = new BatchDTO();
            dto.setId(batch.getBatchId());
            dto.setTrainerId(batch.getTrainerId());
            dto.setStatus(batch.getStatus());
            dto.setCourseNames(courseNames);

            return ResponseEntity.ok(dto);
        }

        @Operation(summary = "Delete Batch",
                description = "Deletes an existing batch by its ID. " +
                        "If the batch exists, it is removed and the deleted batch details are returned. " +
                        "If the batch does not exist, a 404 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batch deleted successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response",
                                                value = "{\n" +
                                                        "  \"id\": 101,\n" +
                                                        "  \"trainerId\": 501,\n" +
                                                        "  \"status\": \"DELETED\",\n" +
                                                        "  \"courseIds\": [201, 202],\n" +
                                                        "  \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "}"
                                        )
                                }
                        )
                ),@ApiResponse(responseCode = "400",
                description = "Invalid ID (negative value)",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDTO.class),
                        examples = {
                                @ExampleObject(
                                        name = "Bad Request Response",
                                        value = "{\n" +
                                                "  \"timestamp\": \"2026-04-17T09:55:00\",\n" +
                                                "  \"message\": \"Batch ID must be non-negative\",\n" +
                                                "  \"errorCode\": \"TES-400\",\n" +
                                                "  \"path\": \"/batches/-5\"\n" +
                                                "}"
                                )
                        }
                )
        ),@ApiResponse(responseCode = "404",
                description = "Batch not found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDTO.class),
                        examples = {
                                @ExampleObject(
                                        name = "Not Found Response",
                                        value = "{\n" +
                                                "  \"timestamp\": \"2026-04-17T09:56:00\",\n" +
                                                "  \"message\": \"Batch with id=999 not found\",\n" +
                                                "  \"errorCode\": \"TES-404\",\n" +
                                                "  \"path\": \"/batches/999\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:06:00\",\n" +
                                                        "  \"message\": \"Failed to delete batch\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/101\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<BatchDTO> deleteBatch(@PathVariable Long id) {
            Batch deletedBatch = batchService.deleteBatch(id);
            return ResponseEntity.ok(BatchMapper.toDTO(deletedBatch, getCourseIds(deletedBatch.getBatchId())));
        }


        @Operation(summary = "Update Batch Status",
                description = "Updates the status of an existing batch by its ID. " +
                        "If the batch exists, its status is updated and the updated batch details are returned. " +
                        "If the batch does not exist or the status value is invalid, appropriate error responses are returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batch status updated successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response",
                                                value = "{\n" +
                                                        "  \"id\": 101,\n" +
                                                        "  \"trainerId\": 501,\n" +
                                                        "  \"status\": \"COMPLETED\",\n" +
                                                        "  \"courseIds\": [201, 202],\n" +
                                                        "  \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "400",
                        description = "Invalid ID (negative value)",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Bad Request Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-17T09:55:00\",\n" +
                                                        "  \"message\": \"Batch ID must be non-negative\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/-5\"\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "404",
                        description = "Batch not found",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Not Found Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:11:00\",\n" +
                                                        "  \"message\": \"Batch with id=999 not found\",\n" +
                                                        "  \"errorCode\": \"B404\",\n" +
                                                        "  \"path\": \"/batches/999/status?status=ACTIVE\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:12:00\",\n" +
                                                        "  \"message\": \"Failed to update batch status\",\n" +
                                                        "  \"errorCode\": \"B500\",\n" +
                                                        "  \"path\": \"/batches/101/status\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @PutMapping("/{id}/status")
        public ResponseEntity<BatchDTO> updateBatchStatus(@PathVariable Long id, @RequestParam BatchStatus status) {
            Batch updatedBatch = batchService.updateStatusById(id, status);
            return ResponseEntity.ok(BatchMapper.toDTO(updatedBatch, getCourseIds(updatedBatch.getBatchId())));
        }

        @Operation(summary = "Get Batches by Status",
                description = "Retrieves all batches that match the given status. " +
                        "If the status is valid and batches exist, it returns a list of BatchDTOs. " +
                        "If no batches match, the response is still 200 with an empty list. " +
                        "If the status value is invalid (not part of the BatchStatus enum), a 400 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batches retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response with Data",
                                                value = "[\n" +
                                                        "  {\n" +
                                                        "    \"id\": 101,\n" +
                                                        "    \"trainerId\": 501,\n" +
                                                        "    \"status\": \"ACTIVE\",\n" +
                                                        "    \"courseIds\": [201, 202],\n" +
                                                        "    \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:16:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batches by status\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/status?status=ACTIVE\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/status")
        public ResponseEntity<List<BatchDTO>> getBatchesByStatus(@RequestParam("status") BatchStatus status) {
            List<BatchDTO> batchDTOs = batchService.getBatchesByStatus(status).stream()
                    .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                    .toList();
            return ResponseEntity.ok(batchDTOs);
        }

        @Operation(summary = "Get Batches by Course ID",
                description = "Retrieves all batches associated with a given course ID. " +
                        "If batches exist for the course, they are returned as a list of BatchDTOs. " +
                        "If no batches exist, the response is still 200 with an empty list. " +
                        "If the courseId is invalid (e.g., null or negative), a 400 error is returned. " +
                        "If the course does not exist, a 404 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batches retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response with Data",
                                                value = "[\n" +
                                                        "  {\n" +
                                                        "    \"id\": 101,\n" +
                                                        "    \"trainerId\": 501,\n" +
                                                        "    \"status\": \"ACTIVE\",\n" +
                                                        "    \"courseIds\": [201],\n" +
                                                        "    \"courseNames\": [\"Java Basics\"]\n" +
                                                        "  },\n" +
                                                        "  {\n" +
                                                        "    \"id\": 102,\n" +
                                                        "    \"trainerId\": 502,\n" +
                                                        "    \"status\": \"PLANNED\",\n" +
                                                        "    \"courseIds\": [201],\n" +
                                                        "    \"courseNames\": [\"Java Basics\"]\n" +
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
                        description = "Invalid course ID provided",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Bad Request - Invalid Course ID",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:35:00\",\n" +
                                                        "  \"message\": \"Course ID must be positive\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/course?course_id=-1\"\n" +
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
                                                name = "Not Found Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:36:00\",\n" +
                                                        "  \"message\": \"Course with id=999 not found\",\n" +
                                                        "  \"errorCode\": \"TES-404\",\n" +
                                                        "  \"path\": \"/batches/course?course_id=999\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:37:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batches by courseId\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/course?course_id=201\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/course")
        public ResponseEntity<List<BatchDTO>> getBatchesByCourseId(@RequestParam("course_id") Long courseId) {
            List<BatchDTO> batchDTOs = batchService.getBatchesByCourseId(courseId).stream()
                    .map(batch -> BatchMapper.toDTO(batch, getCourseIds(batch.getBatchId())))
                    .toList();
            return ResponseEntity.ok(batchDTOs);
        }

        @Operation(summary = "Get Batches by Trainer ID",
                description = "Retrieves all batches assigned to the given trainer. " +
                        "If the trainer has batches, they are returned as a list of BatchDTOs. " +
                        "If no batches exist for the trainer, the response is still 200 with an empty list. " +
                        "If the trainerId is invalid (e.g., null or negative), a 400 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batches retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response with Data",
                                                value = "[\n" +
                                                        "  {\n" +
                                                        "    \"id\": 101,\n" +
                                                        "    \"trainerId\": 501,\n" +
                                                        "    \"status\": \"ACTIVE\",\n" +
                                                        "    \"courseIds\": [201, 202],\n" +
                                                        "    \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "  },\n" +
                                                        "  {\n" +
                                                        "    \"id\": 102,\n" +
                                                        "    \"trainerId\": 501,\n" +
                                                        "    \"status\": \"PLANNED\",\n" +
                                                        "    \"courseIds\": [203],\n" +
                                                        "    \"courseNames\": [\"Python Fundamentals\"]\n" +
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
                        description = "Invalid trainerId provided",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Bad Request - Invalid Trainer ID",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:20:00\",\n" +
                                                        "  \"message\": \"Trainer ID must be positive\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/trainer?trainer_id=-1\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:21:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batches by trainerId\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/trainer?trainer_id=501\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/trainer")
        public ResponseEntity<List<BatchDTO>> getBatchesByTrainerId( @RequestParam("trainer_id") Long trainerId) {
            List<BatchDTO> batchDTOs = batchService.getBatchByTrainerId(trainerId).stream()
                    .map(batch -> {

                        List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batch.getBatchId());

                        List<String> courseNames = courseIds.stream()
                                .map(courseServiceClient::getCourseById)
                                .map(CourseResponseDTO::getTitle)
                                .collect(Collectors.toList());

                        BatchDTO dto = new BatchDTO();
                        dto.setId(batch.getBatchId());
                        dto.setTrainerId(batch.getTrainerId());
                        dto.setCourseIds(courseIds);
                        dto.setCourseNames(courseNames);
                        dto.setStatus(batch.getStatus());
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(batchDTOs);
        }

        @Operation(summary = "Get Batch Details by ID",
                description = "Retrieves detailed information for a specific batch by its ID. " +
                        "If the batch exists, its details are returned including trainer, status, start/end dates, and course names. " +
                        "If the batch does not exist, a 404 error is returned. " +
                        "If the provided ID is invalid (e.g., null or negative), a 400 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Batch details retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = BatchDetailsDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response",
                                                value = "{\n" +
                                                        "  \"id\": 101,\n" +
                                                        "  \"trainerId\": 501,\n" +
                                                        "  \"status\": \"ACTIVE\",\n" +
                                                        "  \"startDate\": \"2026-04-16T09:00:00\",\n" +
                                                        "  \"endDate\": \"2026-06-16T17:00:00\",\n" +
                                                        "  \"courseIds\": [201, 202],\n" +
                                                        "  \"courseNames\": [\"Java Basics\", \"Spring Boot\"]\n" +
                                                        "}"
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
                                                name = "Bad Request - Invalid ID",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:24:00\",\n" +
                                                        "  \"message\": \"Batch ID must be positive\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/-1/details\"\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "404",
                        description = "Batch not found",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Not Found Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:25:00\",\n" +
                                                        "  \"message\": \"Batch with id=999 not found\",\n" +
                                                        "  \"errorCode\": \"TES-404\",\n" +
                                                        "  \"path\": \"/batches/999/details\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:26:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve batch details\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/101/details\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/{id}/details")
        public ResponseEntity<BatchDetailsDTO> getBatchDetails( @PathVariable Long id) {
            Batch batchDetails = batchService.getBatchDetailsById(id);

            List<Long> courseIds = courseBatchMapDAO.findCourseIdsByBatchId(batchDetails.getBatchId());
            List<String> courseNames = courseIds.stream()
                    .map(courseServiceClient::getCourseById)
                    .map(CourseResponseDTO::getTitle)
                    .collect(Collectors.toList());

            BatchDetailsDTO dto = new BatchDetailsDTO();
            dto.setId(batchDetails.getBatchId());
            dto.setTrainerId(batchDetails.getTrainerId());
            dto.setStatus(batchDetails.getStatus());
            dto.setCourseNames(courseNames);
            dto.setStartDate(batchDetails.getStartDate());
            dto.setEndDate(batchDetails.getEndDate());

            return ResponseEntity.ok(dto);
        }

        private List<Long> getCourseIds(Long batchId) {
            return courseBatchMapDAO.findCourseIdsByBatchId(batchId);
        }

        @Operation(summary = "Get Courses for Batch",
                description = "Retrieves all courses associated with a specific batch by its ID. " +
                        "If the batch exists, the list of courses is returned. " +
                        "If no courses are mapped, the response is still 200 with an empty list. " +
                        "If the batch ID is invalid (e.g., negative), a 400 error is returned. " +
                        "If the batch does not exist, a 404 error is returned.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",
                        description = "Courses retrieved successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = CourseResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Successful Response with Data",
                                                value = "[\n" +
                                                        "  {\n" +
                                                        "    \"id\": 201,\n" +
                                                        "    \"title\": \"Java Basics\",\n" +
                                                        "    \"description\": \"Introduction to Java programming\"\n" +
                                                        "  },\n" +
                                                        "  {\n" +
                                                        "    \"id\": 202,\n" +
                                                        "    \"title\": \"Spring Boot\",\n" +
                                                        "    \"description\": \"Building REST APIs with Spring Boot\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:30:00\",\n" +
                                                        "  \"message\": \"Batch ID must be positive\",\n" +
                                                        "  \"errorCode\": \"TES-400\",\n" +
                                                        "  \"path\": \"/batches/-1/courses\"\n" +
                                                        "}"
                                        )
                                }
                        )
                ),
                @ApiResponse(responseCode = "404",
                        description = "Batch not found",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponseDTO.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Not Found Response",
                                                value = "{\n" +
                                                        "  \"timestamp\": \"2026-04-16T16:31:00\",\n" +
                                                        "  \"message\": \"Batch with id=999 not found\",\n" +
                                                        "  \"errorCode\": \"TES-404\",\n" +
                                                        "  \"path\": \"/batches/999/courses\"\n" +
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
                                                        "  \"timestamp\": \"2026-04-16T16:32:00\",\n" +
                                                        "  \"message\": \"Failed to retrieve courses for batch\",\n" +
                                                        "  \"errorCode\": \"TES-500\",\n" +
                                                        "  \"path\": \"/batches/101/courses\"\n" +
                                                        "}"
                                        )
                                }
                        )
                )
        })
        @GetMapping("/{batchId}/courses")
        public ResponseEntity<List<CourseResponseDTO>> getCoursesForBatch( @PathVariable Long batchId) {
            List<CourseResponseDTO> courses = batchService.getCoursesForBatch(batchId);
            return ResponseEntity.ok(courses);
        }
    }

