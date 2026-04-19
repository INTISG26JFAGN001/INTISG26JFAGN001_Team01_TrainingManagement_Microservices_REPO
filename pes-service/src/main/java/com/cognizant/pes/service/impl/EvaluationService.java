package com.cognizant.pes.service.impl;

import com.cognizant.pes.dao.impl.EvaluationDAOImpl;
import com.cognizant.pes.domain.Evaluation;
import com.cognizant.pes.dto.request.EvaluationRequestDTO;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;
import com.cognizant.pes.mapper.EvaluationMapper;
import com.cognizant.pes.service.IEvaluationService;
import lombok.extern.slf4j.Slf4j;   // ✅ Lombok logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EvaluationService implements IEvaluationService {

    @Autowired
    private EvaluationDAOImpl evaluationDAO;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public List<EvaluationResponseDTO> getEvaluationsByBatch(Long batchId) {
        log.info("Fetching evaluations for batchId: {}", batchId);
        return evaluationDAO.findByBatchId(batchId)
                .stream()
                .map(evaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationResponseDTO getAssociateEvaluation(Long batchId, Long associateId) {
        log.info("Fetching evaluation for batchId: {} and associateId: {}", batchId, associateId);
        Evaluation evaluation = evaluationDAO.findByBatchIdAndAssociateId(batchId, associateId);
        if (evaluation == null) {
            log.warn("No evaluation found for batchId: {} and associateId: {}", batchId, associateId);
            return null;
        }
        return evaluationMapper.toDto(evaluation);
    }

    @Override
    @Transactional
    public void calculateBatchPerformance(Long batchId) {
        log.info("Calculating batch performance for batchId: {}", batchId);
        List<Evaluation> evaluations = evaluationDAO.findByBatchId(batchId);

        for (Evaluation eval : evaluations) {
            String status = eval.getFinalScore() >= 60 ? "PASS" : "IN_PROGRESS";
            eval.setOverallStatus(status);
            evaluationDAO.save(eval);
            log.debug("Updated evaluation for associateId: {} with status: {}", eval.getAssociateId(), status);
        }
    }

    @Override
    public EvaluationResponseDTO submitEvaluation(EvaluationRequestDTO request) {
        log.info("Submitting evaluation for associateId: {} in batchId: {}", request.associateId(), request.batchId());
        Evaluation evaluation = evaluationMapper.toDomain(request);
        Evaluation savedEvaluation = evaluationDAO.save(evaluation);
        log.info("Evaluation submitted successfully with id: {}", savedEvaluation.getId());
        return evaluationMapper.toDto(savedEvaluation);
    }
}
