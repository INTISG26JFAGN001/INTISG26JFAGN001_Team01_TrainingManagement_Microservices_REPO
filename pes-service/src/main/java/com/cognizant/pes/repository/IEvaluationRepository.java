package com.cognizant.pes.repository;

import com.cognizant.pes.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEvaluationRepository extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByBatchId(Long batchId);

    Evaluation findByBatchIdAndAssociateId(Long batchId, Long associateId);
}
