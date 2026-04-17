package com.cognizant.pes.service;

import com.cognizant.pes.dto.request.EvaluationRequestDTO;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;

import java.util.List;

public interface IEvaluationService {
    List<EvaluationResponseDTO> getEvaluationsByBatch(Long batchId);

    EvaluationResponseDTO getAssociateEvaluation(Long batchId, Long associateId);

    void calculateBatchPerformance(Long batchId);

    EvaluationResponseDTO submitEvaluation(EvaluationRequestDTO request);
}
