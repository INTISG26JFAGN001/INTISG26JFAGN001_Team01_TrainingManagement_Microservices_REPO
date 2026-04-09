package com.cognizant.asm.repository;

import com.cognizant.asm.entity.AssessmentResult;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    List<AssessmentResult> findByAssociateId(Long AssociateId);
    List<AssessmentResult> findByAssessmentId(Long assessmentId);
    void deleteByAssessmentId(Long assessmentId);
}