package com.cognizant.asm.repository;

import com.cognizant.asm.entity.Rubric;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface RubricRepository extends JpaRepository<Rubric, Long> {

    List<Rubric> findByAssessmentId(Long assessmentId);
    long countByAssessmentId(Long assessmentId);
    void deleteByAssessmentId(Long assessmentId);
    @Query("SELECT COALESCE(SUM(r.weight), 0) FROM Rubric r WHERE r.assessmentId = :assessmentId")
    Integer sumWeightsByAssessmentId(@Param("assessmentId") Long assessmentId);
}