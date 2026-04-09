package com.cognizant.asm.service;

import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;

import java.util.List;

public interface AssessmentService {

    List<AssessmentSummaryResponse> listAll();
    List<AssessmentSummaryResponse> listByBatch(Long batchId);
    List<AssessmentSummaryResponse> listByType(AssessmentType type);
    List<AssessmentSummaryResponse> listByStatus(AssessmentStatus status);
    List<AssessmentSummaryResponse> listByBatchAndType(Long batchId, AssessmentType type);
    List<AssessmentSummaryResponse> listByBatchAndStatus(Long batchId, AssessmentStatus status);
    AssessmentSummaryResponse updateAssessment(Long assessmentId, UpdateAssessmentRequest request);
    void deleteAssessment(Long assessmentId);
}