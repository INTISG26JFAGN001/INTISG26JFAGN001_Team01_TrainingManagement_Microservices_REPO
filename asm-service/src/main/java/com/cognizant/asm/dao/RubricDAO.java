package com.cognizant.asm.dao;

import com.cognizant.asm.entity.Rubric;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RubricDAO {

    Optional<Rubric> findById(Long id);
    List<Rubric> findAll();
    Rubric save(Rubric rubric);
    void deleteById(Long id);

    List<Rubric> findByAssessmentId(Long assessmentId);
    long countByAssessmentId(Long assessmentId);
    void deleteByAssessmentId(Long assessmentId);
    Integer sumWeightsByAssessmentId(@Param("assessmentId") Long assessmentId);
}