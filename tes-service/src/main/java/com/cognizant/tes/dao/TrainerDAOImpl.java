package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.exception.InvalidTrainerException;
import com.cognizant.tes.repository.ITrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerDAOImpl implements ITrainerDAO{
    private final ITrainerRepository trainerRepository;

    public TrainerDAOImpl(ITrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer findById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new InvalidTrainerException("Trainer not found with id " + id));
    }


    public Trainer deleteById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new InvalidTrainerException("Trainer not found with id " + id));
        trainerRepository.deleteById(id);
        return trainer;
    }


    public Trainer findByUserId(Long userId) {
        return trainerRepository.findByUserId(userId);
    }

    public List<Trainer> findTrainersByTechnologyId(Long id){
        return trainerRepository.findTrainersByTechnologyId(id);
    }
}
