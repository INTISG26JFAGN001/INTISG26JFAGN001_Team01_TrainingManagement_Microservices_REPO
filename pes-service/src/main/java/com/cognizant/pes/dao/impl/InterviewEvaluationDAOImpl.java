package com.cognizant.pes.dao.impl;

import com.cognizant.pes.dao.IInterviewEvaluationDAO;
import com.cognizant.pes.domain.InterviewEvaluation;
import com.cognizant.pes.repository.IInterviewEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InterviewEvaluationDAOImpl implements IInterviewEvaluationDAO {

    @Autowired
    private final IInterviewEvaluationRepository repository;

    public InterviewEvaluationDAOImpl(IInterviewEvaluationRepository repository) {
        this.repository = repository;
    }

    @Override
    public InterviewEvaluation save(InterviewEvaluation evaluation) {
        return repository.save(evaluation);
    }

    @Override
    public Optional<InterviewEvaluation> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<InterviewEvaluation> findByAssessmentIdAndAssociateId(Long assessmentId, Long associateId) {
        return repository.findByAssessmentIdAndAssociateId(assessmentId, associateId);
    }

    @Override
    public List<InterviewEvaluation> findByAssessmentId(Long assessmentId) {
        return repository.findByAssessmentId(assessmentId);
    }

    @Override
    public List<InterviewEvaluation> findByAssociateId(Long associateId) {
        return repository.findByAssociateId(associateId);
    }

    @Override
    public boolean existsByAssessmentIdAndAssociateId(Long assessmentId, Long associateId) {
        return repository.existsByAssessmentIdAndAssociateId(assessmentId, associateId);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
