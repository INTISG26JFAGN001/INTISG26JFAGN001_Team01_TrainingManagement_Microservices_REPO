package com.cognizant.pes.service.impl;

import com.cognizant.pes.dao.impl.EvaluationDAOImpl;
import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;
import com.cognizant.pes.mapper.EvaluationMapper;
import com.cognizant.pes.service.IEvaluationService;
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
        List<Evaluation> evaluations = evaluationDAO.findByBatchId(batchId);

        for (Evaluation eval : evaluations) {

            if (eval.getFinalScore() >= 60) {
                eval.setOverallStatus("PASS");
            } else {
                eval.setOverallStatus("IN_PROGRESS");
            }

            evaluationDAO.save(eval);


        }
    }
}