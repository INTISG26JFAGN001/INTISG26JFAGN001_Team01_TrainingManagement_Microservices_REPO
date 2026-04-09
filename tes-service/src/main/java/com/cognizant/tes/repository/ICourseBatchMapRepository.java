package com.cognizant.tes.repository;

import com.cognizant.tes.entity.CourseBatchMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICourseBatchMapRepository extends JpaRepository<CourseBatchMap, Long> {
    @Query("SELECT cbm.courseId FROM CourseBatchMap cbm WHERE cbm.batchId = :batchId")
    List<Long> findCourseIdsByBatchId(@Param("batchId") Long batchId);
}
