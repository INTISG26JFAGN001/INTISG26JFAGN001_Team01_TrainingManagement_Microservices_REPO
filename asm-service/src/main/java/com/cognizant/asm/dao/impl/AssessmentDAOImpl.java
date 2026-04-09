package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.Assessment;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dao.AssessmentDAO;
import com.cognizant.asm.repository.AssessmentRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AssessmentDAOImpl implements AssessmentDAO {

    private final AssessmentRepository assessmentRepository;

    public AssessmentDAOImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public Optional<Assessment> findById(Long id) {
        return assessmentRepository.findById(id);
    }

    @Override
    public List<Assessment> findAll() {
        return assessmentRepository.findAll();
    }

    @Override
    public Assessment save(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @Override
    public void deleteById(Long id) {
        assessmentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return assessmentRepository.existsById(id);
    }

    @Override
    public List<Assessment> findByBatchId(Long batchId) {
        return assessmentRepository.findByBatchId(batchId);
    }

    @Override
    public List<Assessment> findByStageId(Long stageId) {
        return assessmentRepository.findByStageId(stageId);
    }

    @Override
    public List<Assessment> findByType(AssessmentType type) {
        return assessmentRepository.findByType(type);
    }

    @Override
    public List<Assessment> findByStatus(AssessmentStatus status) {
        return assessmentRepository.findByStatus(status);
    }

    @Override
    public List<Assessment> findByBatchIdAndType(Long batchId, AssessmentType type) {
        return assessmentRepository.findByBatchIdAndType(batchId, type);
    }

    @Override
    public List<Assessment> findByBatchIdAndStatus(Long batchId, AssessmentStatus status) {
        return assessmentRepository.findByBatchIdAndStatus(batchId, status);
    }

    @Override
    public List<Assessment> findByBatchIdAndStageIdAndType(Long batchId, Long stageId, AssessmentType type) {
        return assessmentRepository.findByBatchIdAndStageIdAndType(batchId, stageId, type);
    }
}