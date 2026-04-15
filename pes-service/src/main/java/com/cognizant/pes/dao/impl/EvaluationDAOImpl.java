package com.cognizant.pes.dao.impl;

import com.cognizant.pes.dao.IEvaluationDAO;
import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.repository.IEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EvaluationDAOImpl implements IEvaluationDAO {

    @Autowired
    private IEvaluationRepository evaluationRepository;

    @Override
    public List<Evaluation> findByBatchId(Long batchId) {
        return evaluationRepository.findByBatchId(batchId);
    }

    @Override
    public Evaluation findByBatchIdAndAssociateId(Long batchId, Long associateId) {
        return evaluationRepository.findByBatchIdAndAssociateId(batchId, associateId);
    }

    @Override
    public void save(Evaluation eval) {
        evaluationRepository.save(eval);
    }
}