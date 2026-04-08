package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.dao.InterviewDAO;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.service.InterviewService;
import com.cognizant.asm.mapper.InterviewMapper;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;
import com.cognizant.asm.exception.AssessmentNotFoundException;
import com.cognizant.asm.integration.InterviewResultClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewDAO interviewDAO;
    private final RubricDAO rubricDAO;
    private final InterviewMapper interviewMapper;
    private final InterviewResultClient interviewResultClient;

    public InterviewServiceImpl(InterviewDAO interviewDAO, RubricDAO rubricDAO, InterviewMapper interviewMapper, InterviewResultClient interviewResultClient) {
        this.interviewDAO = interviewDAO;
        this.rubricDAO = rubricDAO;
        this.interviewMapper = interviewMapper;
        this.interviewResultClient = interviewResultClient;
    }

    @Override
    @Transactional
    public InterviewDetailResponse createInterview(CreateInterviewRequest request, Long createdBy) {
        Interview interview = interviewMapper.toEntity(request);
        interview.setCreatedBy(createdBy);
        Interview saved = interviewDAO.save(interview);
        // Fetch rubrics (empty at creation time — trainer adds them separately)
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(saved.getId());
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewDetailResponse getInterviewById(Long interviewId, Long associateId) {
        Interview interview = findInterviewOrThrow(interviewId);
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);

        // Attempt to fetch external result — ALWAYS graceful, never throws
        InterviewResultResponse externalResult = null;
        if (associateId != null) {
            try {
                externalResult = interviewResultClient.fetchResult(interviewId, associateId);
            } catch (Exception ex) {
                externalResult = buildFallbackResult(interviewId, associateId, "External evaluation service is currently unavailable.");
            }
        }
        return interviewMapper.toDetailResponse(interview, rubrics, externalResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listInterviewsByBatch(Long batchId) {
        return interviewDAO.findByBatchId(batchId).stream()
                .map(interviewMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listInterviewsByBatchAndCategory(Long batchId, InterviewCategory category) {
        return interviewDAO.findByBatchIdAndInterviewCategory(batchId, category).stream()
                .map(interviewMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewDetailResponse updateInterview(Long interviewId, UpdateAssessmentRequest request) {
        Interview interview = findInterviewOrThrow(interviewId);

        if (request.getTitle() != null)       interview.setTitle(request.getTitle());
        if (request.getDueDate() != null)     interview.setDueDate(request.getDueDate());
        if (request.getMaxScore() != null)    interview.setMaxScore(request.getMaxScore());
        if (request.getStatus() != null)      interview.setStatus(request.getStatus());

        Interview saved = interviewDAO.save(interview);

        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional
    public void deleteInterview(Long interviewId) {
        Interview interview = findInterviewOrThrow(interviewId);
        interviewDAO.deleteById(interviewId);
    }

    private Interview findInterviewOrThrow(Long interviewId) {
        return interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException("Interview not found with ID: " + interviewId));
    }

    // Build a graceful fallback InterviewResultResponse when external service is unavailable.
    private InterviewResultResponse buildFallbackResult(Long assessmentId, Long associateId, String message) {
        InterviewResultResponse fallback = new InterviewResultResponse();
        fallback.setAssessmentId(assessmentId);
        fallback.setAssociateId(associateId);
        fallback.setFetchedFromExternalService(false);
        fallback.setMessage(message);
        return fallback;
    }
}