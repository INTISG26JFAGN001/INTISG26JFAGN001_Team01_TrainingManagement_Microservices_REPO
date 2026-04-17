package com.cognizant.asm.controller;

import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.service.InterviewService;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;
import com.cognizant.asm.dto.response.ErrorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        name = "Interview Management",
        description = "Manages interview-based assessments. Unlike quizzes, interviews are graded manually by evaluators. "
                + "This service coordinates with the Project & Evaluation Service (PES) to synchronize results."
)
@RestController
@RequestMapping("/assessments/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    private static final String INTERVIEW_DETAIL_EXAMPLE = """
        {
          "id": 2,
          "title": "Backend Interview - INTERIM",
          "type": "INTERVIEW",
          "status": "PUBLISHED",
          "batchId": 1001,
          "stageId": 1,
          "dueDate": "2026-05-18",
          "maxScore": 100,
          "interviewCategory": "INTERIM",
          "scheduledDateTime": "2026-05-18T11:00:00",
          "evaluatorRole": "TECH_LEAD",
          "createdBy": 501,
          "rubrics": [
            { "id": 1, "assessmentId": 2, "criteria": "Technical Knowledge", "weight": 40 },
            { "id": 2, "assessmentId": 2, "criteria": "Project Explanation", "weight": 30 },
            { "id": 3, "assessmentId": 2, "criteria": "Communication & Soft Skills", "weight": 30 }
          ],
          "interviewResult": null
        }
        """;

    @Operation(
            summary = "Schedule a new interview",
            description = "Initializes an interview assessment for a batch. " +
                    "You can specify the interview category (INTERIM or FINAL) and the evaluator role responsible for grading."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Interview assessment created successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewDetailResponse.class),
                            examples = @ExampleObject(value = INTERVIEW_DETAIL_EXAMPLE)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<InterviewDetailResponse> createInterview(@Valid @RequestBody CreateInterviewRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        InterviewDetailResponse response = interviewService.createInterview(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Retrieve interview details",
            description = "Fetches the full details of an interview assessment, "
                    + "including any grading rubrics that evaluate specific criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview details retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewDetailResponse.class),
                            examples = @ExampleObject(value = INTERVIEW_DETAIL_EXAMPLE)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No interview found with the provided ID.")
    })
    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDetailResponse> getInterviewById(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewById(interviewId));
    }

    @Operation(
            summary = "List all interviews for a Batch",
            description = "Retrieves a summary of all scheduled interviews for a specific training batch."
    )
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatch(batchId));
    }

    @Operation(
            summary = "Filter interviews by Category",
            description = "Retrieves interviews for a batch, filtered by their category (INTERIM or FINAL)."
    )
    @GetMapping("/batch/{batchId}/category/{category}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndCategory(@PathVariable Long batchId, @PathVariable InterviewCategory category) {
        return ResponseEntity.ok(interviewService.listInterviewsByBatchAndCategory(batchId, category));
    }

    @Operation(
            summary = "Publish interview assessment",
            description = "Finalizes the interview setup. Before publishing, " +
                    "the system verifies that the total weight of all assigned rubrics equals exactly 100."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview published successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewDetailResponse.class),
                            examples = @ExampleObject(value = INTERVIEW_DETAIL_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Validation failed: Rubric weights must total exactly 100.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timeStamp": "2026-04-17T09:50:00",
                          "status": 422,
                          "error": "Unprocessable Entity",
                          "message": "Interview setup is incomplete. Current criteria weights total 70. You must add 30 more to reach the required 100.",
                          "path": "/assessments/interview/50/publish"
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping("/{interviewId}/publish")
    public ResponseEntity<InterviewDetailResponse> publishInterview(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.publishInterview(interviewId));
    }

    @Operation(
            summary = "Synchronize results from PES",
            description = "Fetches the latest manual grading results for all participants in " +
                    "this interview from the external Project & Evaluation Service (PES)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Results synchronized successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewResultResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        [
                          {
                            "id": 8001,
                            "assessmentId": 2,
                            "associateId": 2486094,
                            "totalScore": 92,
                            "maxScore": 100,
                            "resultStatus": "PASS",
                            "evaluatorId": 505,
                            "evaluatorRole": "TECH_LEAD",
                            "evaluatorRemarks": "Candidate demonstrated exceptional proficiency inSpring Boot.",
                            "evaluatedAt": "2026-05-18T14:30:00",
                            "rubricScores": [
                              { "id": 9001, "rubricId": 1, "criteria": "Technical Knowledge", "weight": 40, "scoreAwarded": 38, "remarks": "Strong Technical Knowledge." },
                              { "id": 9002, "rubricId": 2, "criteria": "Project Explanation", "weight": 30, "scoreAwarded": 28, "remarks": "Strong knowledge of REST principles." },
                              { "id": 9003, "rubricId": 3, "criteria": "Communication & Soft Skills", "weight": 30, "scoreAwarded": 26, "remarks": "Clear and professional explanation." }
                            ],
                            "fetchedFromExternalService": true,
                            "message": "Results synchronized successfully from the external Project & Evaluation Service."
                          }
                        ]
                        """
                            )
                    )
            )
    })
    @GetMapping("/{interviewId}/results")
    public ResponseEntity<List<InterviewResultResponse>> getInterviewResults(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewResults(interviewId));
    }

    @Operation(
            summary = "Retrieve result for a single associate",
            description = "Fetches the specific grading details, including rubric scores and evaluator remarks, for one student (associate)."
    )
    @GetMapping("/{interviewId}/associate/{associateId}")
    public ResponseEntity<InterviewResultResponse> getAssociateResult(@PathVariable Long interviewId, @PathVariable Long associateId) {
        return ResponseEntity.ok(interviewService.getAssociateResult(interviewId, associateId));
    }
}