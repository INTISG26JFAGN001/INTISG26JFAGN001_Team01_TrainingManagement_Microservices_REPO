package com.cognizant.asm.dto.response;

import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
public class QuizDetailResponse {

    private Long id;
    private String title;
    private AssessmentType type;
    private AssessmentStatus status;
    private Long batchId;
    private Long stageId;
    private LocalDate dueDate;
    private Integer maxScore;
    private Integer durationMinutes;
    private Integer passingMarks;
    private Long createdBy;
    private List<QuizQuestionResponse> questions;
}
