package com.cognizant.tes.service;

import com.cognizant.tes.dao.IBatchDAO;
import com.cognizant.tes.dao.ICourseBatchMapDAO;
import com.cognizant.tes.dto.BatchDetailsDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.entity.CourseBatchMap;
import com.cognizant.tes.exception.InvalidBatchException;
import com.cognizant.tes.mapper.BatchMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BatchServiceImpl implements IBatchService {

    private final IBatchDAO batchDAO;
    private final ICourseBatchMapDAO courseBatchMapDAO;

    public BatchServiceImpl(IBatchDAO batchDAO, ICourseBatchMapDAO courseBatchMapDAO) {
        this.batchDAO = batchDAO;
        this.courseBatchMapDAO = courseBatchMapDAO;
    }
    public List<Batch> getAllBatches() {
        return batchDAO.findAll();
    }

    public Batch addBatch(BatchDetailsDTO batchDTO) {
        Batch batch = BatchMapper.toEntity(batchDTO);
        Batch savedBatch = batchDAO.save(batch);

        List<Long> courseIds = batchDTO.getCourseIds();
        if (courseIds != null) {
            for (Long courseId : courseIds) {
                CourseBatchMap mapping = new CourseBatchMap();
                mapping.setBatchId(savedBatch.getBatchId());
                mapping.setCourseId(courseId);
                courseBatchMapDAO.save(mapping);
            }
        }
        return savedBatch;
    }

    public Batch getBatchById(Long batchId) throws InvalidBatchException {
        return batchDAO.findById(batchId);
    }

    public Batch deleteBatch(Long batchId) {
        return batchDAO.deleteById(batchId);
    }

    public List<Batch> getBatchesByStatus(BatchStatus status) {
        return batchDAO.findByStatus(status);
    }

    public Batch updateStatusById(Long batchId, BatchStatus status) throws InvalidBatchException {
        Batch batch = batchDAO.findById(batchId);
        batch.setStatus(status);
        return batchDAO.save(batch);
    }

    public List<Batch> getBatchesByCourseId(Long courseId) {
        return batchDAO.findByCourseId(courseId);
    }

    public List<Batch> getBatchByTrainerId(Long trainerId) {
        return batchDAO.findByTrainerId(trainerId);
    }

    public Batch getBatchDetailsById(Long id) {
        return batchDAO.findById(id);
    }
}
