package com.cognizant.asm.service;

import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;

import java.util.List;

public interface InterviewService {

    InterviewDetailResponse createInterview(CreateInterviewRequest request, Long createdBy);
    InterviewDetailResponse getInterviewById(Long interviewId);
    List<AssessmentSummaryResponse> listInterviewsByBatch(Long batchId);
    List<AssessmentSummaryResponse> listInterviewsByBatchAndCategory(Long batchId, InterviewCategory category);
    InterviewDetailResponse publishInterview(Long interviewId);
    List<InterviewResultResponse> getInterviewResults(Long interviewId);
    InterviewResultResponse getAssociateResult(Long interviewId, Long associateId);
}