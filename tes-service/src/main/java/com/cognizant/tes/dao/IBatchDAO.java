package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;

import java.util.List;

public interface IBatchDAO {
    Batch save(Batch batch);
    List<Batch> findAll();
    Batch findById(Long id);
    Batch deleteById(Long id);
    List<Batch> findByStatus(BatchStatus status);
    List<Batch> findByCourseId(Long course_id);
    List<Batch> findByTrainerId(Long trainer_id);
}
