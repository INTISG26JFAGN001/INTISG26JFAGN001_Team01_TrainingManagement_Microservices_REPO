package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.Rubric;
import com.cognizant.asm.dao.RubricDAO;
import com.cognizant.asm.repository.RubricRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RubricDAOImpl implements RubricDAO {

    private final RubricRepository rubricRepository;

    public RubricDAOImpl(RubricRepository rubricRepository) {
        this.rubricRepository = rubricRepository;
    }

    @Override
    public Optional<Rubric> findById(Long id) {
        return rubricRepository.findById(id);
    }

    @Override
    public List<Rubric> findAll() {
        return rubricRepository.findAll();
    }

    @Override
    public Rubric save(Rubric rubric) {
        return rubricRepository.save(rubric);
    }

    @Override
    public void deleteById(Long id) {
        rubricRepository.deleteById(id);
    }

    @Override
    public List<Rubric> findByAssessmentId(Long assessmentId) {
        return rubricRepository.findByAssessmentId(assessmentId);
    }

    @Override
    public long countByAssessmentId(Long assessmentId) {
        return rubricRepository.countByAssessmentId(assessmentId);
    }

    @Override
    public void deleteByAssessmentId(Long assessmentId) {
        rubricRepository.deleteByAssessmentId(assessmentId);
    }

    @Override
    public Integer sumWeightsByAssessmentId(Long assessmentId) {
        return rubricRepository.sumWeightsByAssessmentId(assessmentId);
    }
}