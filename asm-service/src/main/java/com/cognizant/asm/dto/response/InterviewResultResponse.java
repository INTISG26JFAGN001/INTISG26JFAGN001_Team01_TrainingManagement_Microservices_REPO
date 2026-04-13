package com.cognizant.asm.dto.response;

import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

// Interview result fetched from an external service (Project & Evaluation Service / PES).
    // This DTO represents the data returned by the InterviewResultClient.

@Data
public class InterviewResultResponse {

    private Long id;
    private Long assessmentId;
    private Long associateId;

    private Integer totalScore;
    private Integer maxScore;
    private String resultStatus;

    private Long evaluatorId;
    private String evaluatorRole;
    private String evaluatorRemarks;
    private LocalDateTime evaluatedAt;

    private List<RubricEvaluationResult> rubricScores;

    private boolean fetchedFromExternalService;
    private String message;

    // per-rubric criterion result
    @Data
    public static class RubricEvaluationResult {

        private Long id;
        private Long rubricId;
        private String criteria;
        private Integer weight;
        private Integer scoreAwarded;
        private String remarks;
    }
}