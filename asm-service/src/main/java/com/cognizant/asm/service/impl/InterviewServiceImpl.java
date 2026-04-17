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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("Request to create Interview for Batch: {} by User: {}", request.getBatchId(), createdBy);
        validateBatchId(request.getBatchId());
        validateTrainer(createdBy);
        Interview interview = interviewMapper.toEntity(request);
        interview.setCreatedBy(createdBy);
        Interview saved = interviewDAO.save(interview);
        log.info("Successfully created Interview with ID: {}", saved.getId());
        // Fetch rubrics (empty at creation time — trainer adds them separately)
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(saved.getId());
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewDetailResponse getInterviewById(Long interviewId) {
        log.debug("Fetching interview details for ID: {}", interviewId);
        Interview interview = findInterviewOrThrow(interviewId);
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);
        return interviewMapper.toDetailResponse(interview, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listInterviewsByBatch(Long batchId) {
        log.info("Listing all interviews for Batch ID: {}", batchId);
        return interviewDAO.findByBatchId(batchId).stream()
                .map(interviewMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listInterviewsByBatchAndCategory(Long batchId, InterviewCategory category) {
        log.info("Filtering interviews for Batch ID: {} and Category: {}", batchId, category);
        return interviewDAO.findByBatchIdAndInterviewCategory(batchId, category).stream()
                .map(interviewMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewDetailResponse publishInterview(Long interviewId) {
        log.info("Attempting to publish Interview ID: {}", interviewId);
        Interview interview =findInterviewOrThrow(interviewId);
        log.debug("Validating rubrics before publication for Interview ID: {}", interviewId);
        rubricService.validateRubricTotalForInterview(interviewId);
        interview.setStatus(AssessmentStatus.PUBLISHED);
        Interview saved = interviewDAO.save(interview);
        log.info("Interview ID: {} has been successfully PUBLISHED", interviewId);
        List<Rubric> rubrics = rubricDAO.findByAssessmentId(interviewId);
        return interviewMapper.toDetailResponse(saved, rubrics, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResultResponse> getInterviewResults(Long interviewId) {
        log.info("Fetching results for all associates for Interview ID: {}", interviewId);
        findInterviewOrThrow(interviewId);
        // Attempt to fetch external result — ALWAYS graceful, never throws
        try {
            List<InterviewResultResponse> results = interviewResultClient.fetchAllResults(interviewId);
            log.info("Successfully retrieved {} results from external service", results.size());
            return results;
        } catch (Exception ex) {
            log.error("External service failed for Interview {}. Reason: {}. Returning fallback list.", interviewId, ex.getMessage());
            return List.of(buildFallbackResult(interviewId, null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResultResponse getAssociateResult(Long interviewId, Long associateId) {
        log.debug("Fetching result for Associate: {} in Interview: {}", associateId, interviewId);
        findInterviewOrThrow(interviewId);
        // Attempt to fetch external result — ALWAYS graceful, never throws
        try {
            return interviewResultClient.fetchResult(interviewId, associateId);
        } catch (Exception ex) {
            log.warn("Could not fetch result for Associate {} in Interview {}. Triggering fallback.", associateId, interviewId);
            return buildFallbackResult(interviewId, associateId);
        }
    }

    private Interview findInterviewOrThrow(Long interviewId) {
        return interviewDAO.findById(interviewId)
                .orElseThrow(() -> {
                    log.error("Interview Lookup Failed: ID {} not found", interviewId);
                    return new AssessmentNotFoundException("Interview not found with ID: " + interviewId);
                });
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
            log.debug("Batch validation successful for ID: {}", batchId);
        } catch (Exception ex) {
            log.error("Batch validation failed for ID: {}. Error: {}", batchId, ex.getMessage());
            throw new BatchNotFoundException(batchId);
        }
    }

    private void validateTrainer(Long trainerId) {
        if (trainerId == null) return;
        try {
            tesUserClient.getTrainerById(trainerId);
            log.debug("Trainer validation successful for ID: {}", trainerId);
        } catch (Exception ex) {
            log.error("Trainer validation failed for ID: {}. Error: {}", trainerId, ex.getMessage());
            throw new UserNotFoundException("Trainer", trainerId);
        }
    }
}