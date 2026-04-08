package com.cognizant.asm.dto.request;

import com.cognizant.asm.enums.AssessmentStatus;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
public class CreateQuizRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotNull(message = "BatchId is required")
    private Long batchId;

    private Long stageId;

    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private Integer maxScore;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Min(value = 0, message = "Passing marks must be >= 0")
    private Integer passingMarks;

    private AssessmentStatus status = AssessmentStatus.DRAFT;

    @NotEmpty(message = "At least one question is required")
    @Valid
    private List<QuizQuestionRequest> questions;
}