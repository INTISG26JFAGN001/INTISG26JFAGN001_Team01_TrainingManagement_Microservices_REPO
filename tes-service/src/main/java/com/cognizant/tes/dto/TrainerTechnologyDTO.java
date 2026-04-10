package com.cognizant.tes.dto;

public class TrainerTechnologyDTO {
    private Long id;
    private Long trainerId;
    private Long technologyId;

    public TrainerTechnologyDTO() {}

    public TrainerTechnologyDTO(Long id, Long trainerId, Long technologyId) {
        this.id = id;
        this.trainerId = trainerId;
        this.technologyId = technologyId;
    }

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

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }

}
