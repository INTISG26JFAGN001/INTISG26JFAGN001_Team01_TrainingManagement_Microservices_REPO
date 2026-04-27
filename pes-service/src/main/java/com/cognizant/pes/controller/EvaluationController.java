package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.EvaluationRequestDTO;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;
import com.cognizant.pes.service.impl.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
@Validated
@Tag(
        name = "Evaluation Controller",
        description = "Handles evaluation and assessment operations for associates. " +
                "Provides APIs to submit evaluations, retrieve evaluations by batch or associate, " +
                "and perform batch-level performance aggregation."
)
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Operation(
            summary = "Submit Evaluation",
            description = "Submits an evaluation for an associate belonging to a specific batch. " +
                    "The evaluation includes interim score, final score, and overall status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluation submitted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationResponseDTO.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"batchId\": 101,\n" +
                                            "  \"associateId\": 3001,\n" +
                                            "  \"interimScore\": 75,\n" +
                                            "  \"finalScore\": 82,\n" +
                                            "  \"overallStatus\": \"PASS\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Batch or Associate not found")
    })
    @PostMapping("/submitEvaluation")
    public ResponseEntity<EvaluationResponseDTO> submitEvaluation(
            @Valid @RequestBody EvaluationRequestDTO requestDTO) {

        EvaluationResponseDTO response =
                evaluationService.submitEvaluation(requestDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Evaluations by Batch",
            description = "Fetches all associate evaluations for the given batch ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluations retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getBatchEvaluations(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(
                evaluationService.getEvaluationsByBatch(batchId)
        );
    }

    @Operation(
            summary = "Get Associate Evaluation",
            description = "Fetches the evaluation details of a specific associate within a specific batch."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluation retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @GetMapping("/batch/{batchId}/associate/{associateId}")
    public ResponseEntity<EvaluationResponseDTO> getAssociateEvaluation(
            @PathVariable Long batchId,
            @PathVariable Long associateId) {

        return ResponseEntity.ok(
                evaluationService.getAssociateEvaluation(batchId, associateId)
        );
    }

    @Operation(
            summary = "Calculate Batch Performance",
            description = "Triggers performance aggregation for the specified batch. " +
                    "This operation runs asynchronously and does not return a response body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Batch performance calculation started"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    @PostMapping("/batch/{batchId}/calculate")
    public ResponseEntity<Void> calculateBatchPerformance(
            @PathVariable Long batchId) {

        evaluationService.calculateBatchPerformance(batchId);
        return ResponseEntity.accepted().build();
    }
}