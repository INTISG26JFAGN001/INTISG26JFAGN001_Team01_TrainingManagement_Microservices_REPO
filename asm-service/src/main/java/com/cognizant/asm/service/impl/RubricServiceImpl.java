package com.cognizant.asm.service.impl;

import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.dao.AssessmentDAO;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.service.RubricService;
import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;
import com.cognizant.asm.mapper.RubricMapper;
import com.cognizant.asm.exception.AssessmentNotFoundException;
import com.cognizant.asm.exception.RubricNotFoundException;
import com.cognizant.asm.exception.RubricWeightExceededException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RubricServiceImpl implements RubricService {

    private final RubricDAO rubricDAO;
    private final AssessmentDAO assessmentDAO;
    private final RubricMapper rubricMapper;

    public RubricServiceImpl(RubricDAO rubricDAO, AssessmentDAO assessmentDAO, RubricMapper rubricMapper) {
        this.rubricDAO = rubricDAO;
        this.assessmentDAO = assessmentDAO;
        this.rubricMapper = rubricMapper;
    }

    @Override
    @Transactional
    public RubricResponse createRubric(Long assessmentId, CreateRubricRequest request) {
        assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));

        Integer currentTotal = rubricDAO.sumWeightsByAssessmentId(assessmentId);
        if (currentTotal == null) currentTotal = 0;
        if (currentTotal + request.getWeight() > 100) {
            throw new RubricWeightExceededException(currentTotal, request.getWeight());
        }

        Rubric rubric = rubricMapper.toEntity(request, assessmentId);
        Rubric saved = rubricDAO.save(rubric);
        return rubricMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubricResponse> getRubricsByAssessment(Long assessmentId) {
        assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
        return rubricDAO.findByAssessmentId(assessmentId)
                .stream()
                .map(rubricMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalWeight(Long assessmentId) {
        assessmentDAO.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException(assessmentId));
        Integer total = rubricDAO.sumWeightsByAssessmentId(assessmentId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional
    public void deleteRubric(Long rubricId) {
        Rubric rubric = rubricDAO.findById(rubricId)
                .orElseThrow(() -> new RubricNotFoundException(rubricId));
        rubricDAO.deleteById(rubricId);
    }
}