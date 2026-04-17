package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Assessment;
import com.cognizant.asm.entity.Quiz;
import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dao.AssessmentDAO;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.service.AssessmentService;
import com.cognizant.asm.mapper.QuizMapper;
import com.cognizant.asm.mapper.InterviewMapper;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.exception.AssessmentNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentDAO assessmentDAO;
    private final RubricDAO rubricDAO;
    private final QuizMapper quizMapper;
    private final InterviewMapper interviewMapper;

    public AssessmentServiceImpl(AssessmentDAO assessmentDAO, RubricDAO rubricDAO, QuizMapper quizMapper, InterviewMapper interviewMapper) {
        this.assessmentDAO = assessmentDAO;
        this.rubricDAO = rubricDAO;
        this.quizMapper = quizMapper;
        this.interviewMapper = interviewMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listAll() {
        log.info("Fetching all assessments");
        return assessmentDAO.findAll()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatch(Long batchId) {
        log.info("Fetching assessments for Batch ID: {}", batchId);
        return assessmentDAO.findByBatchId(batchId)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByType(AssessmentType type) {
        log.debug("Filtering assessments by Type: {}", type);
        return assessmentDAO.findByType(type)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByStatus(AssessmentStatus status) {
        log.debug("Filtering assessments by Status: {}", status);
        return assessmentDAO.findByStatus(status)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatchAndType(Long batchId, AssessmentType type) {
        log.info("Fetching assessments for Batch: {} and Type: {}", batchId, type);
        return assessmentDAO.findByBatchIdAndType(batchId, type)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatchAndStatus(Long batchId, AssessmentStatus status) {
        log.info("Fetching assessments for Batch: {} and Status: {}", batchId, status);
        return assessmentDAO.findByBatchIdAndStatus(batchId, status)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentSummaryResponse updateAssessment(Long assessmentId, UpdateAssessmentRequest request) {
        log.info("Request to update metadata for Assessment ID: {}", assessmentId);
        Assessment assessment = assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> {
                    log.error("Update failed: Assessment ID {} not found", assessmentId);
                    return new AssessmentNotFoundException(assessmentId);
                });
        if(request.getTitle() != null) {
            log.debug("Updating title for ID {}: {} -> {}", assessmentId, assessment.getTitle(), request.getTitle());
            assessment.setTitle(request.getTitle());
        }
        if(request.getDueDate() != null){
            assessment.setDueDate(request.getDueDate());
        }
        if(request.getMaxScore() != null){
            assessment.setMaxScore(request.getMaxScore());
        }
        if(request.getStatus() != null){
            log.info("Status change for Assessment {}: {} -> {}", assessmentId, assessment.getStatus(), request.getStatus());
            assessment.setStatus(request.getStatus());
        }
        Assessment saved = assessmentDAO.save(assessment);
        log.info("Successfully updated Assessment ID: {}", assessmentId);
        return toSummary(saved);
    }

    @Override
    @Transactional
    public void deleteAssessment(Long assessmentId) {
        log.warn("Attempting to delete Assessment ID: {}", assessmentId);
        Assessment assessment = assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
        log.info("Cleaning up rubrics for Assessment ID: {}", assessmentId);
        rubricDAO.deleteByAssessmentId(assessmentId);
        assessmentDAO.deleteById(assessmentId);
        log.warn("Assessment ID: {} successfully deleted", assessmentId);
    }

    private AssessmentSummaryResponse toSummary(Assessment assessment) {
        if(assessment instanceof Quiz quiz) {
            return quizMapper.toSummaryResponse(quiz);
        } else if(assessment instanceof Interview interview) {
            return interviewMapper.toSummaryResponse(interview);
        }
        AssessmentSummaryResponse r = new AssessmentSummaryResponse();
        r.setId(assessment.getId());
        r.setTitle(assessment.getTitle());
        r.setType(assessment.getType());
        r.setStatus(assessment.getStatus());
        r.setBatchId(assessment.getBatchId());
        r.setStageId(assessment.getStageId());
        r.setDueDate(assessment.getDueDate());
        r.setMaxScore(assessment.getMaxScore());
        r.setCreatedBy(assessment.getCreatedBy());
        return r;
    }
}