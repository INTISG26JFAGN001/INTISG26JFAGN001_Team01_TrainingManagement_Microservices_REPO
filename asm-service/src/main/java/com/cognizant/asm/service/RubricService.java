package com.cognizant.asm.service;

import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;

import java.util.List;

public interface RubricService {

    RubricResponse createRubric(Long assessmentId, CreateRubricRequest request);
    List<RubricResponse> getRubricsByAssessment(Long assessmentId);
    int getTotalWeight(Long assessmentId);
    void deleteRubric(Long rubricId);
    void validateRubricTotalForInterview(Long interviewId);
}