package com.cognizant.tes.dto;

import java.util.List;

public class TrainerDTO {
    private Long trainerId;
    private Long userId;
    private List<Long> technologyIds;

    public TrainerDTO() {}

    public TrainerDTO(Long trainerId, Long userId, List<Long> technologyIds) {
        this.trainerId = trainerId;
        this.userId = userId;
        this.technologyIds = technologyIds;
    }

    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(List<Long> technologyIds) { this.technologyIds = technologyIds; }
}
