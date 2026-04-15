package com.cognizant.tes.controller;

import com.cognizant.tes.client.ICourseServiceClient;
import com.cognizant.tes.dao.ITrainerTechnologyDAO;
import com.cognizant.tes.dto.TechnologyResponseDTO;
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
    private final ICourseServiceClient courseServiceClient;

    public TrainerController(ITrainerService trainerService, ITrainerTechnologyDAO trainerTechnologyDAO,
                             ICourseServiceClient courseServiceClient) {
        this.trainerService = trainerService;
        this.trainerTechnologyDAO = trainerTechnologyDAO;
        this.courseServiceClient = courseServiceClient;
    }

    @PostMapping
    public TrainerDTO addTrainer(@RequestBody TrainerDTO trainerDTO) {
        Trainer trainer = TrainerMapper.toEntity(trainerDTO);
        Trainer savedTrainer = trainerService.addTrainer(trainer, trainerDTO.getTechnologyIds());
        List<Long> ids = getTechnologyIds(savedTrainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(savedTrainer, ids, names);
    }

    @GetMapping("/{trainerId}")
    public TrainerDTO getTrainerById(@PathVariable Long trainerId) {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        List<Long> ids = getTechnologyIds(trainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(trainer, ids, names);
    }

    @DeleteMapping("/{trainerId}")
    public TrainerDTO deleteTrainer(@PathVariable Long trainerId) {
        Trainer trainer = trainerService.deleteTrainer(trainerId);
        List<Long> ids = getTechnologyIds(trainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(trainer, ids, names);
    }


    @GetMapping
    public List<TrainerDTO> getAllTrainers() {
        return trainerService.getAllTrainers().stream()
                .map(trainer -> {
                    List<Long> ids = getTechnologyIds(trainer.getTrainerId());
                    List<String> names = ids.stream()
                            .map(id -> courseServiceClient.getTechnologyById(id).getName())
                            .toList();
                    return TrainerMapper.toDTO(trainer, ids, names);
                })
                .toList();
    }

    @GetMapping("/technology")
    public List<TrainerDTO> getTrainersByTechnologyId(@RequestParam Long technologyId) {
        return trainerService.getTrainersByTechnologyId(technologyId).stream()
                .map(trainer -> {
                    List<Long> ids = getTechnologyIds(trainer.getTrainerId());
                    List<String> names = ids.stream()
                            .map(id -> courseServiceClient.getTechnologyById(id).getName())
                            .toList();
                    return TrainerMapper.toDTO(trainer, ids, names);
                })
                .toList();
    }

    @PutMapping("/{trainerId}/technologies")
    public TrainerDTO updateTrainerTechnologyIds(@PathVariable Long trainerId,
                                                 @RequestBody List<Long> technologyIds) {
        Trainer updatedTrainer = trainerService.updateTrainerTechnologyIds(trainerId, technologyIds);
        List<Long> ids = getTechnologyIds(updatedTrainer.getTrainerId());
        List<String> names = ids.stream()
                .map(id -> courseServiceClient.getTechnologyById(id).getName())
                .toList();
        return TrainerMapper.toDTO(updatedTrainer, ids, names);
    }

    @GetMapping("/{trainerId}/technologies")
    public List<TechnologyResponseDTO> getTrainerTechnologies(@PathVariable Long trainerId) {
        return trainerService.getTechnologiesForTrainer(trainerId);
    }

    private List<Long> getTechnologyIds(Long trainerId) {
        return trainerTechnologyDAO.findByTrainerId(trainerId).stream()
                .map(t -> t.getTechnologyId())
                .toList();
    }


}
