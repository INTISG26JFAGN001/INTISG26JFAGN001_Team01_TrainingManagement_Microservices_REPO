package com.cognizant.pes.dao;

import com.cognizant.pes.domain.InterviewEvaluation;

import java.util.List;
import java.util.Optional;

public interface IInterviewEvaluationDAO {

    InterviewEvaluation save(InterviewEvaluation evaluation);

    Optional<InterviewEvaluation> findById(Long id);

    Optional<InterviewEvaluation> findByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);

    List<InterviewEvaluation> findByAssessmentId(Long assessmentId);

    List<InterviewEvaluation> findByAssociateId(Long associateId);

    boolean existsByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);

    void deleteById(Long id);
}
