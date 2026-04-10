package com.cognizant.tes.dao;

import com.cognizant.tes.entity.TrainerTechnology;
import com.cognizant.tes.repository.ITrainerTechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerTechnologyDAOImpl implements ITrainerTechnologyDAO {

    private final ITrainerTechnologyRepository trainerTechnologyRepository;

    public TrainerTechnologyDAOImpl(ITrainerTechnologyRepository trainerTechnologyRepository) {
        this.trainerTechnologyRepository = trainerTechnologyRepository;
    }

    public TrainerTechnology save(TrainerTechnology mapping) {
        return trainerTechnologyRepository.save(mapping);
    }

    @Override
    public List<TrainerTechnology> findByTrainerId(Long trainerId) {
        return trainerTechnologyRepository.findByTrainerId(trainerId);
    }

    @Override
    public void deleteByTrainerId(Long trainerId) {
        trainerTechnologyRepository.deleteByTrainerId(trainerId);
    }

}
