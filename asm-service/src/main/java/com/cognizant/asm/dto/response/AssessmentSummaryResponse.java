package com.cognizant.asm.dto.response;

import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssessmentSummaryResponse {

    private Long id;
    private String title;
    private AssessmentType type;
    private AssessmentStatus status;
    private Long batchId;
    private Long stageId;
    private LocalDate dueDate;
    private Integer maxScore;
    private Long createdBy;
}