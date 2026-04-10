package com.cognizant.tes.controller;

import com.cognizant.tes.dao.ITrainerTechnologyDAO;
import com.cognizant.tes.dto.TrainerDTO;
import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.mapper.TrainerMapper;
import com.cognizant.tes.service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final ITrainerService trainerService;
    private final ITrainerTechnologyDAO trainerTechnologyDAO;

    public TrainerController(ITrainerService trainerService, ITrainerTechnologyDAO trainerTechnologyDAO) {
        this.trainerService = trainerService;
        this.trainerTechnologyDAO = trainerTechnologyDAO;
    }

    @PostMapping
    public TrainerDTO addTrainer(@RequestBody TrainerDTO trainerDTO) {
        Trainer trainer = TrainerMapper.toEntity(trainerDTO);
        Trainer savedTrainer = trainerService.addTrainer(trainer, trainerDTO.getTechnologyIds());
        return TrainerMapper.toDTO(savedTrainer, getTechnologyIds(savedTrainer.getTrainerId()));
    }

    @GetMapping("/{trainerId}")
    public TrainerDTO getTrainerById(@PathVariable Long trainerId) {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        return TrainerMapper.toDTO(trainer, getTechnologyIds(trainer.getTrainerId()));
    }

    @DeleteMapping("/{trainerId}")
    public TrainerDTO deleteTrainer(@PathVariable Long trainerId) {
        Trainer trainer = trainerService.deleteTrainer(trainerId);
        return TrainerMapper.toDTO(trainer, getTechnologyIds(trainer.getTrainerId()));
    }

    @GetMapping
    public List<TrainerDTO> getAllTrainers() {
        return trainerService.getAllTrainers().stream()
                .map(trainer -> TrainerMapper.toDTO(trainer, getTechnologyIds(trainer.getTrainerId())))
                .toList();
    }

    @GetMapping("/technology")
    public List<TrainerDTO> getTrainersByTechnologyId(@RequestParam Long technologyId) {
        return trainerService.getTrainersByTechnologyId(technologyId).stream()
                .map(trainer -> TrainerMapper.toDTO(trainer, getTechnologyIds(trainer.getTrainerId())))
                .toList();
    }

    @PutMapping("/{trainerId}/technologies")
    public TrainerDTO updateTrainerTechnologyIds(@PathVariable Long trainerId,
                                                 @RequestBody List<Long> technologyIds) {
        Trainer updatedTrainer = trainerService.updateTrainerTechnologyIds(trainerId, technologyIds);
        return TrainerMapper.toDTO(updatedTrainer, getTechnologyIds(updatedTrainer.getTrainerId()));
    }

    private List<Long> getTechnologyIds(Long trainerId) {
        return trainerTechnologyDAO.findByTrainerId(trainerId).stream()
                .map(t -> t.getTechnologyId())
                .toList();
    }
}
