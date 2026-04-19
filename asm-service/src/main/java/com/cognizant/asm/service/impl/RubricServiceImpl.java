package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dao.InterviewDAO;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.exception.RubricNotFoundException;
import com.cognizant.asm.service.RubricService;
import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;
import com.cognizant.asm.mapper.RubricMapper;
import com.cognizant.asm.exception.AssessmentNotFoundException;
import com.cognizant.asm.exception.RubricWeightExceededException;
import com.cognizant.asm.exception.RubricTotalNotHundredException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RubricServiceImpl implements RubricService {

    private final RubricDAO rubricDAO;
    private final InterviewDAO interviewDAO;
    private final RubricMapper rubricMapper;

    public RubricServiceImpl(RubricDAO rubricDAO, InterviewDAO interviewDAO, RubricMapper rubricMapper) {
        this.rubricDAO = rubricDAO;
        this.interviewDAO = interviewDAO;
        this.rubricMapper = rubricMapper;
    }

    @Override
    @Transactional
    public RubricResponse createRubric(Long interviewId, CreateRubricRequest request) {
        log.info("Request to add rubric '{}' with weight {}% to Interview ID: {}", request.getCriteria(), request.getWeight(), interviewId);
        Interview interview = interviewDAO.findById(interviewId)
                .orElseThrow(() -> {
                    log.error("Rubric creation failed: Interview ID {} not found", interviewId);
                    return new AssessmentNotFoundException("Interview not found with ID: " + interviewId);
                });

        if(interview.getStatus() ==  AssessmentStatus.PUBLISHED) {
            log.warn("Blocked attempt to add rubric to already PUBLISHED Interview ID: {}", interviewId);
            throw new IllegalStateException("Cannot add rubrics to a PUBLISHED interview");
        }

        Integer currentTotal = rubricDAO.sumWeightsByAssessmentId(interviewId);
        if (currentTotal == null) currentTotal = 0;
        log.debug("Current total weight for Interview {}: {}%. Adding: {}%", interviewId, currentTotal, request.getWeight());
        if (currentTotal + request.getWeight() > 100) {
            log.error("Validation failed for Interview {}: New total would be {}% (Exceeds 100%)", interviewId, (currentTotal + request.getWeight()));
            throw new RubricWeightExceededException(currentTotal, request.getWeight());
        }

        Rubric rubric = rubricMapper.toEntity(request, interviewId);
        Rubric saved = rubricDAO.save(rubric);
        log.info("Successfully saved Rubric ID: {} for Interview: {}", saved.getId(), interviewId);
        return rubricMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubricResponse> getRubricsByAssessment(Long interviewId) {
        log.debug("Fetching all rubrics for Interview ID: {}", interviewId);
        interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException(interviewId));
        return rubricDAO.findByAssessmentId(interviewId)
                .stream()
                .map(rubricMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalWeight(Long interviewId) {
        log.debug("Calculating total weight sum for Interview ID: {}", interviewId);
        interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException(interviewId));
        Integer total = rubricDAO.sumWeightsByAssessmentId(interviewId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional
    public void deleteRubric(Long rubricId) {
        log.warn("Request to delete Rubric ID: {}", rubricId);
        Rubric rubric = rubricDAO.findById(rubricId)
                .orElseThrow(() -> {
            log.error("Delete failed: Rubric ID {} not found", rubricId);
            return new RubricNotFoundException(rubricId);
        });
        rubricDAO.deleteById(rubricId);
        log.info("Successfully deleted Rubric ID: {}", rubricId);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRubricTotalForInterview(Long interviewId) {
        log.debug("Validating rubric total for Interview: {}", interviewId);
        int currentTotal = getTotalWeight(interviewId);
        if(currentTotal != 100) {
            log.error("Interview {} validation failed: total rubric weight is {} (expected 100)", interviewId, currentTotal);
            throw new RubricTotalNotHundredException(interviewId, currentTotal);
        }
        log.info("Interview {} validated successfully with 100% rubric weight", interviewId);
    }
}