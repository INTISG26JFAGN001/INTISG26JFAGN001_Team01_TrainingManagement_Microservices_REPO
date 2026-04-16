package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.dao.InterviewDAO;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.service.InterviewService;
import com.cognizant.asm.service.RubricService;
import com.cognizant.asm.mapper.InterviewMapper;
import com.cognizant.asm.dto.request.CreateInterviewRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.InterviewDetailResponse;
import com.cognizant.asm.dto.response.InterviewResultResponse;
import com.cognizant.asm.exception.AssessmentNotFoundException;
import com.cognizant.asm.exception.UserNotFoundException;
import com.cognizant.asm.exception.BatchNotFoundException;

import com.cognizant.asm.integration.TesUserClient;
import com.cognizant.asm.integration.TesBatchClient;
import com.cognizant.asm.integration.InterviewResultClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewDAO interviewDAO;
    private final RubricDAO rubricDAO;
    private final RubricService rubricService;
    private final InterviewMapper interviewMapper;
    private final TesUserClient tesUserClient;
    private final TesBatchClient tesBatchClient;
    private final InterviewResultClient interviewResultClient;

    public InterviewServiceImpl(InterviewDAO interviewDAO, RubricDAO rubricDAO, RubricService rubricService, InterviewMapper interviewMapper, TesUserClient tesUserClient, TesBatchClient tesBatchClient, InterviewResultClient interviewResultClient) {
        this.interviewDAO = interviewDAO;
        this.rubricDAO = rubricDAO;
        this.rubricService = rubricService;
        this.interviewMapper = interviewMapper;
        this.tesUserClient = tesUserClient;
        this.tesBatchClient = tesBatchClient;
        this.interviewResultClient = interviewResultClient;
    }

    @Override
    @Transactional
    public InterviewDetailResponse createInterview(CreateInterviewRequest request, Long createdBy) {
        validateBatchId(request.getBatchId());
        validateTrainer(createdBy);
        Interview interview = interviewMapper.toEntity(request);
        interview.setCreatedBy(createdBy);
        Interview saved = interviewDAO.save(interview);
        // Fetch rubrics (empty at creation time — trainer adds them separately)
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(saved.getId());
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewDetailResponse getInterviewById(Long interviewId) {
        Interview interview = findInterviewOrThrow(interviewId);
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);
        return interviewMapper.toDetailResponse(interview, rubrics, null);
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
    public InterviewDetailResponse publishInterview(Long interviewId) {
        Interview interview =findInterviewOrThrow(interviewId);
        rubricService.validateRubricTotalForInterview(interviewId);
        interview.setStatus(AssessmentStatus.PUBLISHED);
        Interview saved = interviewDAO.save(interview);
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResultResponse> getInterviewResults(Long interviewId) {
        findInterviewOrThrow(interviewId);
        // Attempt to fetch external result — ALWAYS graceful, never throws
        try {
            return interviewResultClient.fetchAllResults(interviewId);
        } catch (Exception ex) {
            return List.of(buildFallbackResult(interviewId, null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResultResponse getAssociateResult(Long interviewId, Long associateId) {
        findInterviewOrThrow(interviewId);
        // Attempt to fetch external result — ALWAYS graceful, never throws
        try {
            return interviewResultClient.fetchResult(interviewId, associateId);
        } catch (Exception ex) {
            return buildFallbackResult(interviewId, associateId);
        }
    }

    private Interview findInterviewOrThrow(Long interviewId) {
        return interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException("Interview not found with ID: " + interviewId));
    }

    // Build a graceful fallback InterviewResultResponse when external service is unavailable.
    private InterviewResultResponse buildFallbackResult(Long assessmentId, Long associateId) {
        InterviewResultResponse fallback = new InterviewResultResponse();
        fallback.setAssessmentId(assessmentId);
        fallback.setAssociateId(associateId);
        fallback.setFetchedFromExternalService(false);
        fallback.setMessage("External evaluation service is currently unavailable.");
        return fallback;
    }

    private void validateBatchId(Long batchId) {
        if (batchId == null) return;
        try {
            tesBatchClient.getBatchById(batchId);
        } catch (Exception ex) {
            throw new BatchNotFoundException(batchId);
        }
    }

    private void validateTrainer(Long trainerId) {
        if (trainerId == null) return;
        try {
            tesUserClient.getTrainerById(trainerId);

        } catch (Exception ex) {
            throw new UserNotFoundException("Trainer", trainerId);
        }
    }
}