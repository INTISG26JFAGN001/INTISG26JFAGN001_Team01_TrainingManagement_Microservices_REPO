package com.cognizant.asm.controller;

import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.service.AssessmentService;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.ErrorResponseDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import jakarta.validation.Valid;

@Tag(
        name = "General Assessment Management",
        description = "Provides centralized endpoints for managing and querying both quizzes and interviews."
                + " Use these operations to retrieve list-level information, filter across batches, "
                + "update basic assessment metadata and delete assessment"
)
@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    private static final String ASSESSMENT_LIST_EXAMPLE = """
        [
          {
            "id": 1,
            "title": "Java Foundations",
            "type": "QUIZ",
            "status": "PUBLISHED",
            "batchId": 1001,
            "stageId": 1,
            "dueDate": "2026-05-15",
            "createdBy": 501
          },
          {
            "id": 2,
            "title": "Backend Interview - INTERIM",
            "type": "INTERVIEW",
            "status": "DRAFT",
            "batchId": 1001,
            "stageId": 1,
            "dueDate": "2026-05-18",
            "createdBy": 501
          }
        ]
        """;

    @Operation(
            summary = "Retrieve all assessments",
            description = "Fetches a complete list of every quiz and interview registered in the system. "
                    + "This provides a high-level summary of all assessments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the assessment list.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssessmentSummaryResponse.class),
                            examples = @ExampleObject(value = ASSESSMENT_LIST_EXAMPLE)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<AssessmentSummaryResponse>> listAll() {
        return ResponseEntity.ok(assessmentService.listAll());
    }

    @Operation(
            summary = "Retrieve assessments by Batch",
            description = "Filters the assessment list to show only those assigned to a specific training batch ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of assessments for the batch retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssessmentSummaryResponse.class),
                            examples = @ExampleObject(value = ASSESSMENT_LIST_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No batch was found with the provided ID.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timeStamp": "2026-04-17T09:35:00",
                          "status": 404,
                          "error": "Not Found",
                          "message": "The requested training batch does not exist.",
                          "path": "/assessments/batch/9999",
                          "fieldErrors": null
                        }
                        """
                            )
                    )
            )
    })
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(assessmentService.listByBatch(batchId));
    }

    @Operation(
            summary = "Filter assessments by Type",
            description = "Allows you to view only Quizzes or only Interviews across the entire system."
    )
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByType(@PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentService.listByType(type));
    }

    @Operation(
            summary = "Filter assessments by Status",
            description = "Allows you to see assessments based on whether they are in 'DRAFT' or have been 'PUBLISHED'."
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByStatus(@PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(assessmentService.listByStatus(status));
    }

    @Operation(
            summary = "Advanced Filter: Batch and Type",
            description = "Retrieves assessments for a specific batch, further filtered by their type (QUIZ/INTERVIEW)."
    )
    @GetMapping("/batch/{batchId}/type/{type}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndType(@PathVariable Long batchId, @PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentService.listByBatchAndType(batchId, type));
    }

    @Operation(
            summary = "Advanced Filter: Batch and Status",
            description = "Retrieves assessments for a specific batch, further filtered by their current status (DRAFT/PUBLISHED)."
    )
    @GetMapping("/batch/{batchId}/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndStatus(@PathVariable Long batchId, @PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(assessmentService.listByBatchAndStatus(batchId, status));
    }


    @Operation(
            summary = "Update assessment details",
            description = "Updates general metadata for an assessment, such as the title, status, or due date. "
                    + "Use this to finalize a draft or extend a deadline."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Assessment updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssessmentSummaryResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "title": "Java Foundations",
                          "type": "QUIZ",
                          "status": "PUBLISHED",
                          "batchId": 1001,
                          "stageId": 1,
                          "dueDate": "2026-06-01",
                          "createdBy": 501
                        }
                        """
                            )
                    )
            )
    })
    @PatchMapping("/{assessmentId}")
    public ResponseEntity<AssessmentSummaryResponse> updateAssessment(@PathVariable Long assessmentId, @Valid @RequestBody UpdateAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.updateAssessment(assessmentId, request));
    }

    @Operation(
            summary = "Delete an assessment",
            description = "Permanently removes an assessment from the system by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assessment deleted successfully."),
            @ApiResponse(responseCode = "404", description = "No assessment found with the provided ID.")
    })
    @DeleteMapping("/assessmentId")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
        return ResponseEntity.noContent().build();
    }
}