package com.cognizant.pes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record InterviewEvaluationResponseDTO(
        Long id,

        Long assessmentId,

        Long associateId,

        Long evaluatorId,

        String evaluatorRole,

        String evaluatorRemarks,

        Integer totalScore,

        Integer maxScore,

        String resultStatus,

        LocalDateTime evaluatedAt,

        List<RubricScoreResponseDTO> rubricScores
) {
    public record RubricScoreResponseDTO(
            Long id,

            Long rubricId,

            String criteria,

            Integer weight,

            Integer scoreAwarded,

            String remarks
    ) {}
}
