package com.cognizant.pes.repository;

import com.cognizant.pes.domain.InterviewEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {

    Optional<InterviewEvaluation> findByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);

    List<InterviewEvaluation> findByAssessmentId(Long assessmentId);

    List<InterviewEvaluation> findByAssociateId(Long associateId);

    boolean existsByAssessmentIdAndAssociateId(Long assessmentId, Long associateId);
}
