package com.cognizant.pes.service;

import com.cognizant.pes.dao.EvaluationDAOImpl;
import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.domain.Review;
import com.cognizant.pes.dto.EvaluationResponseDTO;
import com.cognizant.pes.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationService implements IEvaluationService {

    @Autowired
    private EvaluationDAOImpl evaluationDAO;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public List<EvaluationResponseDTO> getEvaluationsByBatch(Long batchId) {
        // Fetching all evaluations for US-09: Batch Evaluation Report
        return evaluationDAO.findByBatchId(batchId)
                .stream()
                .map(evaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationResponseDTO getAssociateEvaluation(Long batchId, Long associateId) {
        Evaluation evaluation = evaluationDAO.findByBatchIdAndAssociateId(batchId, associateId);
        return evaluationMapper.toDto(evaluation);
    }

    @Override
    @Transactional
    public void calculateBatchPerformance(Long batchId) {
        // 1. Fetch all associates/evaluations for this batch
        List<Evaluation> evaluations = evaluationDAO.findByBatchId(batchId);

        for (Evaluation eval : evaluations) {
            // 2. Logic: Aggregate scores from the project_reviews table
            // In a microservices environment, you might also fetch Assessment scores here.

            // Example calculation logic:
            // double avgProjectScore = reviewDAO.findAverageScoreByAssociate(eval.getAssociateId());
            // eval.setInterimScore(avgProjectScore);

            // 3. Determine Overall Status
            if (eval.getFinalScore() >= 60) {
                eval.setOverallStatus("PASS");
            } else {
                eval.setOverallStatus("IN_PROGRESS");
            }

            evaluationDAO.save(eval);

            // KAFKA: Publish 'EvaluationUpdated' event for the reporting/notification services
            // publishEvaluationUpdate(eval);
        }
    }
}