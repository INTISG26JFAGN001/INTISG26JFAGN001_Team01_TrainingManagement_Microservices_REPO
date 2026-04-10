package com.cognizant.tes.dao;

import com.cognizant.tes.entity.CourseBatchMap;
import com.cognizant.tes.repository.ICourseBatchMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseBatchMapDAOImpl implements ICourseBatchMapDAO{

    private final ICourseBatchMapRepository courseBatchMapRepository;

    public CourseBatchMapDAOImpl(ICourseBatchMapRepository courseBatchMapRepository) {
        this.courseBatchMapRepository = courseBatchMapRepository;
    }

    public CourseBatchMap save(CourseBatchMap mapping) {
        return courseBatchMapRepository.save(mapping);
    }

    public List<Long> findCourseIdsByBatchId(Long batchId) {
        return courseBatchMapRepository.findCourseIdsByBatchId(batchId);
    }
}
