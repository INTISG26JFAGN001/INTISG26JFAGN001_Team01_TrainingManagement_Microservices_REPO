package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.EnrollmentDTO;
import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.Enrollment;

public class EnrollmentMapper {

    public static EnrollmentDTO toDTO(Enrollment enrollment) {
        if (enrollment == null) return null;

        Long batchId = null;
        if (enrollment.getBatch() != null) {
            batchId = enrollment.getBatch().getBatchId();
        }

        return new EnrollmentDTO(
                enrollment.getEnrollmentId(),
                batchId,
                enrollment.getAssociateId(),
                enrollment.getStatus(),
                enrollment.getJoinDate()
        );
    }

//    public static EnrollmentDTO toDTO(Enrollment enrollment) {
//        EnrollmentDTO dto = new EnrollmentDTO();
//        dto.setId(enrollment.getId());
//        dto.setAssociateId(enrollment.getAssociateId());
//        if (enrollment.getBatch() != null) {
//            dto.setBatchId(enrollment.getBatch().getBatchId());
//        }
//        return dto;
//    }

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
