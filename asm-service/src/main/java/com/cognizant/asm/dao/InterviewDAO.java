package com.cognizant.asm.dao;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.enums.InterviewCategory;

import java.util.List;
import java.util.Optional;

public interface InterviewDAO {

    Optional<Interview> findById(Long id);
    List<Interview> findAll();
    Interview save(Interview interview);

    List<Interview> findByBatchId(Long batchId);
    List<Interview> findByBatchIdAndInterviewCategory(Long batchId, InterviewCategory category);
}