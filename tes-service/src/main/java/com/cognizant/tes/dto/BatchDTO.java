package com.cognizant.tes.dto;

import com.cognizant.tes.entity.BatchStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchDTO {
    private Long id;

    private Long trainerId;
    private BatchStatus status;

    @NotEmpty(message = "Course IDs must be provided")
    private List<Long> courseIds;
    private List<String> courseNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

    public List<String> getCourseNames() {
        return courseNames;
    }

    public void setCourseNames(List<String> courseNames) {
        this.courseNames = courseNames;
    }
}
