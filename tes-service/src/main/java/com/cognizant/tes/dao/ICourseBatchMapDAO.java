package com.cognizant.tes.dao;

import com.cognizant.tes.entity.CourseBatchMap;

import java.util.List;

public interface ICourseBatchMapDAO {

    CourseBatchMap save(CourseBatchMap mapping);
    List<Long> findCourseIdsByBatchId(Long batchId);

}
