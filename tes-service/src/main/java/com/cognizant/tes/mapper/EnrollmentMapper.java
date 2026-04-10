package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.Enrollment;

public class EnrollmentMapper {

    public static EnrollmentDTO toDTO(Enrollment enrollment) {
        if (enrollment == null) return null;
        return new EnrollmentDTO(
                enrollment.getEnrollmentId(),
                enrollment.getBatch().getBatchId(),
                enrollment.getAssociateId(),
                enrollment.getStatus(),
                enrollment.getJoinDate()
        );
    }

    public static Enrollment toEntity(EnrollmentDTO dto, Batch batch) {
        if (dto == null) return null;
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(dto.getEnrollmentId());
        enrollment.setBatch(batch);
        enrollment.setAssociateId(dto.getAssociateId());
        enrollment.setStatus(dto.getStatus());
        enrollment.setJoinDate(dto.getJoinDate());
        return enrollment;
    }
}
