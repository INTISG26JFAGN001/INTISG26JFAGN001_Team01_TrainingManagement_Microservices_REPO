package com.cognizant.tes.dao;

import com.cognizant.tes.entity.TrainerTechnology;
import java.util.List;

public interface ITrainerTechnologyDAO {
    TrainerTechnology save(TrainerTechnology mapping);
    List<TrainerTechnology> findByTrainerId(Long trainerId);
    void deleteByTrainerId(Long trainerId);
}
