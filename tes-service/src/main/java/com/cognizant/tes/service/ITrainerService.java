package com.cognizant.tes.service;

import com.cognizant.tes.entity.Trainer;

import java.util.List;

public interface ITrainerService {

    Trainer addTrainer(Trainer trainer, List<Long> technologyIds);
    Trainer getTrainerById(Long trainerId);
    Trainer deleteTrainer(Long trainerId);
    List<Trainer> getAllTrainers();
    List<Trainer> getTrainersByTechnologyId(Long technologyId);
    Trainer updateTrainerTechnologyIds(Long trainerId, List<Long> technologyIds);

}
