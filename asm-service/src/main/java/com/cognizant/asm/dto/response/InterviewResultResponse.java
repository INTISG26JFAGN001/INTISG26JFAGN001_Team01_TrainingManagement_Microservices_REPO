package com.cognizant.asm.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

    //Interview result fetched from an external service (Project & Evaluation Service / PES).
    // This DTO represents the data returned by the InterviewResultClient.

@Data
public class InterviewResultResponse {

    private Long interviewResultId;
    private Long assessmentId;
    private Long associateId;
    private Integer score;
    private String feedback;
    private Long evaluatorId;
    private String evaluatorRole;
    private LocalDateTime evaluatedAt;

    private boolean fetchedFromExternalService;
    private String message;
}