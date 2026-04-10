package com.cognizant.tes.service;

import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;

import java.util.List;

public interface IEnrollmentService {
    Enrollment createEnrollment(EnrollmentDTO dto);
    Enrollment getEnrollmentById(Long id);
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getEnrollmentsByBatchId(Long batchId);
    Enrollment getEnrollmentByAssociateId(Long associateId);
    List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status);
    Enrollment updateStatus(Long id, EnrollmentStatus status);
    Enrollment deleteEnrollment(Long id);
}
