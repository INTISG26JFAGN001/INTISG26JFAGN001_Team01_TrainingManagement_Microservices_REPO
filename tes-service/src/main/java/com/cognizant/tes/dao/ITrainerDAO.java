package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Trainer;

import java.util.List;

public interface ITrainerDAO {
    Trainer deleteById(Long id);
    Trainer findById(Long id);
    Trainer save(Trainer trainer);
    List<Trainer> findAll();
    Trainer findByUserId(Long userId);
    List<Trainer> findTrainersByTechnologyId(Long id);
}
