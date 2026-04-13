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
        Interview interview = interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException("Interview not found with ID: " + interviewId));

        if(interview.getStatus() ==  AssessmentStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot add rubrics to a PUBLISHED interview");
        }

        Integer currentTotal = rubricDAO.sumWeightsByAssessmentId(interviewId);
        if (currentTotal == null) currentTotal = 0;
        if (currentTotal + request.getWeight() > 100) {
            throw new RubricWeightExceededException(currentTotal, request.getWeight());
        }

        Rubric rubric = rubricMapper.toEntity(request, interviewId);
        Rubric saved = rubricDAO.save(rubric);
        return rubricMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubricResponse> getRubricsByAssessment(Long interviewId) {
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
        interviewDAO.findById(interviewId)
                .orElseThrow(() -> new AssessmentNotFoundException(interviewId));
        Integer total = rubricDAO.sumWeightsByAssessmentId(interviewId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional
    public void deleteRubric(Long rubricId) {
        Rubric rubric = rubricDAO.findById(rubricId)
                .orElseThrow(() -> new RubricNotFoundException(rubricId));
        rubricDAO.deleteById(rubricId);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRubricTotalForInterview(Long interviewId) {
        int currentTotal = getTotalWeight(interviewId);
        if(currentTotal != 100) {
            throw new RubricTotalNotHundredException(interviewId, currentTotal);
        }
    }
}