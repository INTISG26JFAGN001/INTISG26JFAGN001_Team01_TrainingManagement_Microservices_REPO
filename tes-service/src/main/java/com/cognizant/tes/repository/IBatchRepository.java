package com.cognizant.tes.repository;

import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBatchRepository extends JpaRepository<Batch,Long> {

    List<Batch> findByStatus(BatchStatus status);

    List<Batch> findByTrainerId(Long trainer_Id);

    @Query("SELECT b FROM Batch b JOIN CourseBatchMap cbm ON " +
            "b.batchId = cbm.batchId WHERE cbm.courseId = :courseId")
    List<Batch> findBatchesByCourseId(@Param("courseId") Long courseId);

    // Query(sql for getting data from the course_batch table)
    // course_id | batch_id
    // 1    |   2
    // 1    |   3
}
