package com.cognizant.asm.dao;

import com.cognizant.asm.entity.Assessment;
import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;

import java.util.List;
import java.util.Optional;

public interface AssessmentDAO {

    Optional<Assessment> findById(Long id);
    List<Assessment> findAll();
    Assessment save(Assessment assessment);
    void deleteById(Long id);
    boolean existsById(Long id);

    List<Assessment> findByBatchId(Long batchId);
    List<Assessment> findByStageId(Long stageId);
    List<Assessment> findByType(AssessmentType Type);
    List<Assessment> findByStatus(AssessmentStatus status);
    List<Assessment> findByBatchIdAndType(Long batchId, AssessmentType type);
    List<Assessment> findByBatchIdAndStatus(Long batchId, AssessmentStatus status);
    List<Assessment> findByBatchIdAndStageIdAndType(Long batchId, Long stageId, AssessmentType type);

}