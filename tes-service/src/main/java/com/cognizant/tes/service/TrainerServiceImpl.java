package com.cognizant.tes.service;

import com.cognizant.tes.client.ICourseServiceClient;
import com.cognizant.tes.dao.ITrainerDAO;
import com.cognizant.tes.dao.ITrainerTechnologyDAO;
import com.cognizant.tes.dto.TechnologyResponseDTO;
import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.entity.TrainerTechnology;
import com.cognizant.tes.exception.InvalidArgumentException;
import com.cognizant.tes.exception.InvalidTrainerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainerServiceImpl implements ITrainerService {


    private ITrainerDAO trainerDAO;
    private ITrainerTechnologyDAO trainerTechnologyDAO;
    private final ICourseServiceClient courseServiceClient;

    public TrainerServiceImpl(ITrainerDAO trainerDAO, ITrainerTechnologyDAO trainerTechnologyDAO, ICourseServiceClient courseServiceClient) {
            this.trainerDAO = trainerDAO;
            this.trainerTechnologyDAO = trainerTechnologyDAO;
            this.courseServiceClient = courseServiceClient;
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
        if (trainerId < 0) {
            throw new InvalidArgumentException("Trainer ID must be non-negative");
        }
        return trainerDAO.findById(trainerId);
    }

    public Trainer deleteTrainer(Long trainerId) {
        if (trainerId < 0) {
            throw new InvalidArgumentException("Trainer ID must be non-negative");
        }
        return trainerDAO.deleteById(trainerId);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    public List<Trainer> getTrainersByTechnologyId(Long technologyId) {
        if (technologyId < 0) {
            throw new InvalidArgumentException("Technology ID must be non-negative");
        }
        return trainerDAO.findTrainersByTechnologyId(technologyId);
    }

    @Transactional
    public Trainer updateTrainerTechnologyIds(Long trainerId, List<Long> technologyIds) {
        if (trainerId < 0) {
            throw new InvalidArgumentException("Trainer ID must be non-negative");
        }
        Trainer trainer = trainerDAO.findById(trainerId);


        List<TrainerTechnology> existingMappings = trainerTechnologyDAO.findByTrainerId(trainerId);
        List<Long> existingIds = existingMappings.stream()
                .map(TrainerTechnology::getTechnologyId)
                .toList();

        List<Long> newIds = (technologyIds == null) ? Collections.emptyList() : technologyIds;


        for (Long techId : newIds) {
            if (!existingIds.contains(techId)) {
                TrainerTechnology mapping = new TrainerTechnology();
                mapping.setTrainerId(trainerId);
                mapping.setTechnologyId(techId);
                trainerTechnologyDAO.save(mapping);
            }
        }

        return trainer;
    }

    public List<TechnologyResponseDTO> getTechnologiesForTrainer(Long trainerId) {
        if (trainerId < 0) {
            throw new InvalidArgumentException("Batch ID must be non-negative");
        }

        Trainer trainerOpt = trainerDAO.findById(trainerId);
        if (trainerOpt==null) {
            throw new InvalidTrainerException("Trainer with id=" + trainerId + " not found");
        }

        List<Long> technologyIds = trainerTechnologyDAO.findByTrainerId(trainerId)
                .stream()
                .map(TrainerTechnology::getTechnologyId)
                .toList();

        return technologyIds.stream()
                .map(courseServiceClient::getTechnologyById)
                .toList();
    }

}
