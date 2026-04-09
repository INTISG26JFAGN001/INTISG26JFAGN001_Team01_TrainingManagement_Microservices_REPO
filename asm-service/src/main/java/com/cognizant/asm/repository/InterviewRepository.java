package com.cognizant.asm.repository;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.enums.InterviewCategory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByBatchId(Long batchId);
    List<Interview> findByBatchIdAndInterviewCategory(Long batchId, InterviewCategory category);
}