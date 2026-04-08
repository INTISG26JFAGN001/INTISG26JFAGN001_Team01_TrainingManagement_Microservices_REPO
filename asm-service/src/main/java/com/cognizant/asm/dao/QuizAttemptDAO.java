package com.cognizant.asm.dao;

import com.cognizant.asm.entity.QuizAttempt;

import java.util.List;
import java.util.Optional;

public interface QuizAttemptDAO {

    Optional<QuizAttempt> findById(Long id);
    List<QuizAttempt> findAll();
    QuizAttempt save(QuizAttempt attempt);
    void deleteById(Long id);

    List<QuizAttempt> findByAssociateId(Long associateId);
    List<QuizAttempt> findByAssessmentId(Long assessmentId);
    boolean existsByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);
    Optional<QuizAttempt> findByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);
}