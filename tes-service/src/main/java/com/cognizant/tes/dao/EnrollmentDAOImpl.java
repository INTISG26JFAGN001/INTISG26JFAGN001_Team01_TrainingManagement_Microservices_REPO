package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;
import com.cognizant.tes.exception.InvalidEnrollmentException;
import com.cognizant.tes.repository.IEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentDAOImpl implements IEnrollmentDAO{


    private final IEnrollmentRepository enrollmentRepository;

    public EnrollmentDAOImpl(IEnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(()->
                new InvalidEnrollmentException("Invalid enrollment id")
        );
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> findByBatchId(Long batchId) {
        List<Enrollment> enrollments= enrollmentRepository.findByBatch_BatchId(batchId);
        if (enrollments.isEmpty()) {
            throw new InvalidEnrollmentException("No enrollments found for batch id " + batchId);
        }
        return enrollments;
    }

    public Enrollment findByAssociateId(Long associateId) {
        Enrollment enrollment= enrollmentRepository.findByAssociateId(associateId);
        if (enrollment == null) {
            throw new InvalidEnrollmentException("No enrollment found for associate id " + associateId);
        }
        return enrollment;
    }

    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status);
    }

     public Enrollment updateStatus(Long id, EnrollmentStatus newStatus) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();
        enrollment.setStatus(newStatus);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment deleteById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()->
                new InvalidEnrollmentException("Enrollment not found with id"+id));

        enrollmentRepository.delete(enrollment);

        return enrollment;
    }
}
