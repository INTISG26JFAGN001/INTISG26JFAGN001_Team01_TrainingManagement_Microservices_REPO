package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Assessment;
import com.cognizant.asm.entity.Quiz;
import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dao.AssessmentDAO;
import com.cognizant.asm.dao.AssessmentResultDAO;
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

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentDAO assessmentDAO;
    private final AssessmentResultDAO assessmentResultDAO;
    private final RubricDAO rubricDAO;
    private final QuizMapper quizMapper;
    private final InterviewMapper interviewMapper;

    public AssessmentServiceImpl(AssessmentDAO assessmentDAO, AssessmentResultDAO assessmentResultDAO, RubricDAO rubricDAO, QuizMapper quizMapper, InterviewMapper interviewMapper) {
        this.assessmentDAO = assessmentDAO;
        this.assessmentResultDAO = assessmentResultDAO;
        this.rubricDAO = rubricDAO;
        this.quizMapper = quizMapper;
        this.interviewMapper = interviewMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listAll() {
        return assessmentDAO.findAll()
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatch(Long batchId) {
        return assessmentDAO.findByBatchId(batchId)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByType(AssessmentType type) {
        return assessmentDAO.findByType(type)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByStatus(AssessmentStatus status) {
        return assessmentDAO.findByStatus(status)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatchAndType(Long batchId, AssessmentType type) {
        return assessmentDAO.findByBatchIdAndType(batchId, type)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> listByBatchAndStatus(Long batchId, AssessmentStatus status) {
        return assessmentDAO.findByBatchIdAndStatus(batchId, status)
                .stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentSummaryResponse updateAssessment(Long assessmentId, UpdateAssessmentRequest request) {
        Assessment assessment = assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
        if(request.getTitle() != null) {
            assessment.setTitle(request.getTitle());
        }
        if(request.getDueDate() != null){
            assessment.setDueDate(request.getDueDate());
        }
        if(request.getMaxScore() != null){
            assessment.setMaxScore(request.getMaxScore());
        }
        if(request.getStatus() != null){
            assessment.setStatus(request.getStatus());
        }
        Assessment saved = assessmentDAO.save(assessment);
        return toSummary(saved);
    }

    @Override
    @Transactional
    public void deleteAssessment(Long assessmentId) {
        Assessment assessment = assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
        rubricDAO.deleteByAssessmentId(assessmentId);
        assessmentResultDAO.deleteByAssessmentId(assessmentId);
        assessmentDAO.deleteById(assessmentId);
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