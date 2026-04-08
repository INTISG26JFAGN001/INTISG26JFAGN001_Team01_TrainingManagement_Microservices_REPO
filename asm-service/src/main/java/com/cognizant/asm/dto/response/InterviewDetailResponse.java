package com.cognizant.asm.dto.response;

import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.InterviewCategory;

import lombok.Data;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InterviewDetailResponse {

    private Long id;
    private String title;
    private AssessmentType type;
    private AssessmentStatus status;
    private Long batchId;
    private Long stageId;
    private LocalDate dueDate;
    private Integer maxScore;
    private InterviewCategory interviewCategory;
    private Integer totalMarks;
    private LocalDateTime scheduledDateTime;
    private String evaluatorRole;
    private Long createdBy;
    private List<RubricResponse> rubrics;
    private InterviewResultResponse interviewResult;
}