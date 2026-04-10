package com.cognizant.tes.repository;

import com.cognizant.tes.entity.TrainerTechnology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITrainerTechnologyRepository extends JpaRepository<TrainerTechnology, Long> {
    List<TrainerTechnology> findByTrainerId(Long trainerId);
    void deleteByTrainerId(Long trainerId);
}
