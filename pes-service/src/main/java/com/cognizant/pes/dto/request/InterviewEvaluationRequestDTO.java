package com.cognizant.pes.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request DTO for submitting an interview evaluation from PES.
 * The evaluator provides scores for each rubric criterion.
 *
 * Flow:
 * 1. PES calls ASM to fetch rubrics for the interview
 * 2. Evaluator fills in scoreAwarded for each rubric criterion
 * 3. PES stores the evaluation and computes totalScore
 */
public record InterviewEvaluationRequestDTO(

        @NotNull(message = "Assessment ID is required")
        Long assessmentId,

        @NotNull(message = "Associate ID is required")
        Long associateId,

        @NotNull(message = "Evaluator ID is required")
        Long evaluatorId,

        String evaluatorRole,

        String evaluatorRemarks,

        @NotEmpty(message = "At least one rubric score is required")
        @Valid
        List<RubricScoreDTO> rubricScores

) {
    /**
     * Per-criterion score submitted by the evaluator.
     */
    public record RubricScoreDTO(

            @NotNull(message = "Rubric ID is required")
            Long rubricId,

            // criteria and weight are fetched from ASM — included here for validation/snapshot
            String criteria,

            @NotNull(message = "Rubric weight is required")
            @Min(value = 0) @Max(value = 100)
            Integer weight,

            @NotNull(message = "Score awarded is required")
            @Min(value = 0, message = "Score awarded cannot be negative")
            Integer scoreAwarded,

            String remarks
    ) {}
}
