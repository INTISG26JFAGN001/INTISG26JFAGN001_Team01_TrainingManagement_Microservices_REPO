package com.cognizant.pes.dao;

import com.cognizant.pes.domain.Evaluation;

import java.util.Arrays;
import java.util.List;

public interface IEvaluationDAO {
    List<Evaluation> findByBatchId(Long batchId);

    Evaluation findByBatchIdAndAssociateId(Long batchId, Long associateId);

    Evaluation save(Evaluation eval);

}
