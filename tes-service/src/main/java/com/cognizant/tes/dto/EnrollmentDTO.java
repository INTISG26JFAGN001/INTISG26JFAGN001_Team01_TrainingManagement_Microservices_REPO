package com.cognizant.tes.dto;

import com.cognizant.tes.entity.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDTO {
    private Long enrollmentId;
    private Long batchId;
    private Long associateId;
    private EnrollmentStatus status;
    private LocalDate joinDate;

    public EnrollmentDTO(Long enrollmentId, Long batchId, Long associateId,
                         EnrollmentStatus status, LocalDate joinDate) {
        this.enrollmentId =enrollmentId;
        this.batchId = batchId;
        this.associateId = associateId;
        this.status = status;
        this.joinDate = joinDate;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
}
