package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.InterviewEvaluationRequestDTO;
import com.cognizant.pes.dto.response.InterviewEvaluationResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.service.IInterviewEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview-evaluations")
@Tag(
        name = "Interview Evaluation Controller",
        description = "Handles interview evaluation submissions and retrievals within PES. " +
                "Evaluations are performed against rubric criteria obtained from ASM and include " +
                "per-criterion scoring, total score computation, and final result determination."
)
public class InterviewEvaluationController {

    private final IInterviewEvaluationService evaluationService;

    public InterviewEvaluationController(IInterviewEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Operation(
            summary = "Submit Interview Evaluation",
            description = "Submits an interview evaluation for an associate for a specific assessment. " +
                    "The evaluator provides scores for each rubric criterion, and PES calculates " +
                    "the total and maximum scores to determine the result."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Interview evaluation submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewEvaluationResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"id\": 501,\n" +
                                            "  \"assessorId\": 1001,\n" +
                                            "  \"associateId\": 3001,\n" +
                                            "  \"evaluatorId\": 9001,\n" +
                                            "  \"evaluatorRole\": \"PANELIST\",\n" +
                                            "  \"totalScore\": 78,\n" +
                                            "  \"maxScore\": 100,\n" +
                                            "  \"resultStatus\": \"PASS\",\n" +
                                            "  \"evaluatedAt\": \"2026-04-17T10:15:00\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Assessment, Associate or Rubric not found")
    })
    @PostMapping
    public ResponseEntity<InterviewEvaluationResponseDTO> submitEvaluation(
            @Valid @RequestBody InterviewEvaluationRequestDTO request)
            throws ResourceNotFoundException {

        InterviewEvaluationResponseDTO response =
                evaluationService.submitEvaluation(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get Interview Evaluation by Assessment and Associate",
            description = "Fetches the interview evaluation of a specific associate for a given assessment."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview evaluation retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewEvaluationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @GetMapping("/assessment/{assessmentId}/associate/{associateId}")
    public ResponseEntity<InterviewEvaluationResponseDTO> getEvaluation(
            @PathVariable Long assessmentId,
            @PathVariable Long associateId)
            throws ResourceNotFoundException {

        return ResponseEntity.ok(
                evaluationService.getEvaluationByAssessmentAndAssociate(
                        assessmentId, associateId)
        );
    }

    @Operation(
            summary = "Get All Interview Evaluations by Assessment",
            description = "Retrieves all interview evaluations conducted for the specified assessment."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview evaluations retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewEvaluationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Assessment not found")
    })
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssessment(
            @PathVariable Long assessmentId) {

        return ResponseEntity.ok(
                evaluationService.getAllEvaluationsByAssessment(assessmentId)
        );
    }

    @Operation(
            summary = "Get All Interview Evaluations by Associate",
            description = "Retrieves all interview evaluations received by a specific associate across assessments."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview evaluations retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterviewEvaluationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Associate not found")
    })
    @GetMapping("/associate/{associateId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssociate(
            @PathVariable Long associateId) {

        return ResponseEntity.ok(
                evaluationService.getAllEvaluationsByAssociate(associateId)
        );
    }
}
