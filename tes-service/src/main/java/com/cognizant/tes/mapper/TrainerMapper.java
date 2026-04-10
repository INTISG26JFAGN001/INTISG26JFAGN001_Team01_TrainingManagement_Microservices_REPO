package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.TrainerDTO;
import com.cognizant.tes.dto.TrainerTechnologyDTO;
import com.cognizant.tes.entity.Trainer;
import com.cognizant.tes.entity.TrainerTechnology;

import java.util.List;

public class TrainerMapper {



    public static TrainerDTO toDTO(Trainer trainer, List<Long> technologyIds) {
        if (trainer == null) return null;
        TrainerDTO dto = new TrainerDTO();
        dto.setTrainerId(trainer.getTrainerId());
        dto.setUserId(trainer.getUserId());
        dto.setTechnologyIds(technologyIds);
        return dto;
    }


    public static Trainer toEntity(TrainerDTO dto) {
        if (dto == null) return null;
        Trainer trainer = new Trainer();
        trainer.setTrainerId(dto.getTrainerId());
        trainer.setUserId(dto.getUserId());
        return trainer;
    }

    public static TrainerTechnologyDTO toTechnologyDTO(TrainerTechnology trainerTechnology) {
        if (trainerTechnology == null) return null;
        TrainerTechnologyDTO dto = new TrainerTechnologyDTO();
        dto.setId(trainerTechnology.getId());
        dto.setTrainerId(trainerTechnology.getTrainerId());
        dto.setTechnologyId(trainerTechnology.getTechnologyId());
        return dto;
    }
}
