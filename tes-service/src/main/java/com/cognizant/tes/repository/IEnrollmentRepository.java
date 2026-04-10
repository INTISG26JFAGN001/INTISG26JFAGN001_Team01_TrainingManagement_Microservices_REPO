package com.cognizant.tes.repository;

import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEnrollmentRepository extends JpaRepository<Enrollment,Long> {

    List<Enrollment> findByBatch_BatchId(Long batchId);

    Enrollment findByAssociateId(Long associateId);

    List<Enrollment> findByStatus(EnrollmentStatus status);
}
