package com.cognizant.asm.repository;

import com.cognizant.asm.entity.QuizAttempt;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByAssociateId(Long associateId);
    List<QuizAttempt> findByAssessmentId(Long assessmentId);
    boolean existsByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);
    Optional<QuizAttempt> findByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);
}