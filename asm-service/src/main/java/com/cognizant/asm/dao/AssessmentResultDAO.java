package com.cognizant.asm.dao;

import com.cognizant.asm.entity.AssessmentResult;

import java.util.List;
import java.util.Optional;

public interface AssessmentResultDAO {

    Optional<AssessmentResult> findById(Long id);
    List<AssessmentResult> findAll();
    AssessmentResult save(AssessmentResult result);
    void deleteById(Long id);

    List<AssessmentResult> findByAssociateId(Long AssociateId);
    List<AssessmentResult> findByAssessmentId(Long assessmentId);
    void deleteByAssessmentId(Long assessmentId);
}
