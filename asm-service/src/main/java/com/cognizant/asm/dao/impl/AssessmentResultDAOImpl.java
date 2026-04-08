package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.AssessmentResult;
import com.cognizant.asm.dao.AssessmentResultDAO;
import com.cognizant.asm.repository.AssessmentResultRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AssessmentResultDAOImpl implements AssessmentResultDAO {

    private final AssessmentResultRepository assessmentResultRepository;

    public AssessmentResultDAOImpl(AssessmentResultRepository assessmentResultRepository) {
        this.assessmentResultRepository = assessmentResultRepository;
    }

    @Override
    public Optional<AssessmentResult> findById(Long id) {
        return assessmentResultRepository.findById(id);
    }

    @Override
    public List<AssessmentResult> findAll() {
        return assessmentResultRepository.findAll();
    }

    @Override
    public AssessmentResult save(AssessmentResult result) {
        return assessmentResultRepository.save(result);
    }

    @Override
    public void deleteById(Long id) {
        assessmentResultRepository.deleteById(id);
    }

    @Override
    public List<AssessmentResult> findByAssociateId(Long associateId) {
        return assessmentResultRepository.findByAssociateId(associateId);
    }

    @Override
    public List<AssessmentResult> findByAssessmentId(Long assessmentId) {
        return assessmentResultRepository.findByAssessmentId(assessmentId);
    }

    @Override
    public void deleteByAssessmentId(Long assessmentId) {
        assessmentResultRepository.deleteByAssessmentId(assessmentId);
    }
}