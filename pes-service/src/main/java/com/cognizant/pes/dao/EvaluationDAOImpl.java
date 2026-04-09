package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.repository.IEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EvaluationDAOImpl implements IEvaluationDAO {

    @Autowired
    private IEvaluationRepository evaluationRepository;

    @Override
    public List<Evaluation> findByBatchId(Long batchId) {
        // Returns all associate evaluations for a specific batch (Used for US-09 reports)
        return evaluationRepository.findByBatchId(batchId);
    }

    @Override
    public Evaluation findByBatchIdAndAssociateId(Long batchId, Long associateId) {
        // Returns a specific evaluation record for an associate in a batch
        return evaluationRepository.findByBatchIdAndAssociateId(batchId, associateId);
    }

    @Override
    public void save(Evaluation eval) {
        // Persists or updates the evaluation record in the 'evaluation' table
        evaluationRepository.save(eval);
    }
}