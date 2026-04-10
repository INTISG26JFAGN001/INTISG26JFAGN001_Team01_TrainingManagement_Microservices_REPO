package com.cognizant.tes.service;

import com.cognizant.tes.dao.ITrainerDAO;
import com.cognizant.tes.dao.ITrainerTechnologyDAO;
import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.entity.TrainerTechnology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TrainerServiceImpl implements ITrainerService {


    private ITrainerDAO trainerDAO;
    private ITrainerTechnologyDAO trainerTechnologyDAO;

    public TrainerServiceImpl(ITrainerDAO trainerDAO, ITrainerTechnologyDAO trainerTechnologyDAO) {
            this.trainerDAO = trainerDAO;
            this.trainerTechnologyDAO = trainerTechnologyDAO;
    }

    public Trainer addTrainer(Trainer trainer, List<Long> technologyIds) {
        Trainer savedTrainer = trainerDAO.save(trainer);

        for (Long techId : technologyIds) {
            TrainerTechnology mapping = new TrainerTechnology();
            mapping.setTrainerId(savedTrainer.getTrainerId());
            mapping.setTechnologyId(techId);
            trainerTechnologyDAO.save(mapping);
        }

        return savedTrainer;
    }

    public Trainer getTrainerById(Long trainerId) {
        return trainerDAO.findById(trainerId);
    }

    public Trainer deleteTrainer(Long trainerId) {
        return trainerDAO.deleteById(trainerId);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    public List<Trainer> getTrainersByTechnologyId(Long technologyId) {
        return trainerDAO.findTrainersByTechnologyId(technologyId);
    }

    @Transactional
    public Trainer updateTrainerTechnologyIds(Long trainerId, List<Long> technologyIds) {
        Trainer trainer = trainerDAO.findById(trainerId); // validates trainer exists

        trainerTechnologyDAO.deleteByTrainerId(trainerId);

        List<Long> ids = (technologyIds == null) ? Collections.emptyList() : technologyIds;
        for (Long techId : ids) {
            TrainerTechnology mapping = new TrainerTechnology();
            mapping.setTrainerId(trainerId);
            mapping.setTechnologyId(techId);
            trainerTechnologyDAO.save(mapping);
        }

        return trainer;
    }

}
