package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;

import java.util.List;

public interface IEnrollmentDAO {
    Enrollment save(Enrollment enrollment);

    Enrollment findById(Long id);

    List<Enrollment> findAll();

    List<Enrollment> findByBatchId(Long batchId);

    Enrollment findByAssociateId(Long associateId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Enrollment updateStatus(Long id, EnrollmentStatus status);

    Enrollment deleteById(Long id);
}
