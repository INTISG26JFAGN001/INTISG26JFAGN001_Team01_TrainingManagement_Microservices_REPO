package com.cognizant.asm.dto.request;

import com.cognizant.asm.enums.AssessmentStatus;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateAssessmentRequest {

    private String title;
    private LocalDate dueDate;
    private Integer maxScore;
    private AssessmentStatus status;
}