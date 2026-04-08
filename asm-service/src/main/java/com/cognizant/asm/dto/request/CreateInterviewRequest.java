package com.cognizant.asm.dto.request;

import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.enums.InterviewCategory;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

@Data
public class CreateInterviewRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    @NotNull(message = "BatchId iis required")
    private Long batchId;

    private Long stageId;

    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private Integer maxScore = 100;

    @NotNull(message = "Interview category (INTERIM/FINAL) is required")
    private InterviewCategory interviewCategory;

    private Integer totalMarks = 100;
    private LocalDateTime scheduledDateTime;

    private String evaluatorRole;

    private AssessmentStatus status = AssessmentStatus.DRAFT;
}