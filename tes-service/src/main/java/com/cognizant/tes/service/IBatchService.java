package com.cognizant.tes.service;

import com.cognizant.tes.dto.BatchDTO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.exception.InvalidBatchException;

import java.util.List;

public interface IBatchService {
    public List<Batch> getAllBatches();
    Batch addBatch(BatchDetailsDTO batch);
    public Batch getBatchById(Long batch_id) throws InvalidBatchException;
    public Batch deleteBatch(Long batch_id);
    public List<Batch> getBatchesByStatus(BatchStatus status);
    public Batch updateStatusById(Long batch_id, BatchStatus status) throws InvalidBatchException;
    public List<Batch> getBatchesByCourseId(Long course_id);
    public List<Batch> getBatchByTrainerId(Long trainer_id);
    public Batch getBatchDetailsById(Long id);
}
