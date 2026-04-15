package com.cognizant.tes.dto;

import java.util.List;

public class TrainerDTO {
    private Long trainerId;
    private Long userId;
    private List<Long> technologyIds;
    private List<String> technologyNames;

    public TrainerDTO() {};

    public TrainerDTO(Long trainerId, Long userId, List<Long> technologyIds, List<String> technologyNames) {
        this.trainerId = trainerId;
        this.userId = userId;
        this.technologyIds = technologyIds;
        this.technologyNames = technologyNames;
    }

    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(List<Long> technologyIds) { this.technologyIds = technologyIds; }

    public List<String> getTechnologyNames() {
        return technologyNames;
    }

    public void setTechnologyNames(List<String> technologyNames) {
        this.technologyNames = technologyNames;
    }
}
