package com.cognizant.asm.service;

import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;

import java.util.List;

public interface InterviewService {

    InterviewDetailResponse createInterview(CreateInterviewRequest request, Long createdBy);
    InterviewDetailResponse getInterviewById(Long interviewId, Long associateId);
    List<AssessmentSummaryResponse> listInterviewsByBatch(Long batchId);
    List<AssessmentSummaryResponse> listInterviewsByBatchAndCategory(Long batchId, InterviewCategory category);
    InterviewDetailResponse updateInterview(Long interviewId, UpdateAssessmentRequest request);
    void deleteInterview(Long interviewId);
}