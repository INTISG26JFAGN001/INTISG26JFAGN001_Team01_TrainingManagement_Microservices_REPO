package com.cognizant.tes.service;

import com.cognizant.tes.dao.IBatchDAO;
import com.cognizant.tes.dao.IEnrollmentDAO;
import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.Enrollment;
import com.cognizant.tes.entity.EnrollmentStatus;
import com.cognizant.tes.exception.InvalidArgumentException;
import com.cognizant.tes.exception.InvalidBatchException;
import com.cognizant.tes.exception.InvalidEnrollmentException;
import com.cognizant.tes.mapper.EnrollmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentServiceImpl implements IEnrollmentService {

    private final IBatchDAO batchDAO;
    private final IEnrollmentDAO enrollmentDAO;

    public EnrollmentServiceImpl(IBatchDAO batchDAO, IEnrollmentDAO enrollmentDAO) {
        this.batchDAO = batchDAO;
        this.enrollmentDAO = enrollmentDAO;
    }

    public Enrollment createEnrollment(EnrollmentDTO dto) {
        Batch batch = batchDAO.findById(dto.getBatchId());
        Enrollment enrollment = EnrollmentMapper.toEntity(dto, batch);
        batch.addEnrollment(enrollment);
        batchDAO.save(batch);
        return enrollment;
    }

    public Enrollment getEnrollmentById(Long id) {
        if (id < 0) {
            throw new InvalidArgumentException("Enrollment ID must be non-negative");
        }

        return enrollmentDAO.findById(id);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    public List<Enrollment> getEnrollmentsByBatchId(Long batchId) {
        if (batchId < 0) {
            throw new InvalidArgumentException("Batch ID must be non-negative");
        }
        return enrollmentDAO.findByBatchId(batchId);
    }

    public Enrollment getEnrollmentByAssociateId(Long associateId) {
        if (associateId < 0) {
            throw new InvalidArgumentException("Associate ID must be non-negative");
        }
        return enrollmentDAO.findByAssociateId(associateId);
    }

    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentDAO.findByStatus(status);
    }

    public Enrollment updateStatus(Long id, EnrollmentStatus status) {
        if (id < 0) {
            throw new InvalidArgumentException("Enrollment ID must be non-negative");
        }
        return enrollmentDAO.updateStatus(id, status);
    }

    public Enrollment deleteEnrollment(Long id) {
        if (id < 0) {
            throw new InvalidArgumentException("Enrollment ID must be non-negative");
        }
        Enrollment enrollment = enrollmentDAO.findById(id);
        if (enrollment == null) {
            throw new InvalidEnrollmentException("Enrollment not found with id " + id);
        }

        Batch batch = enrollment.getBatch();
        if (batch == null) {
            throw new InvalidBatchException("Batch not found for enrollment " + id);
        }

        batch.removeEnrollment(enrollment);
        batchDAO.save(batch);
        return enrollment;
    }
}
