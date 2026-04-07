package com.cognizant.cat.service;

import com.cognizant.cat.dto.StageRequestDTO;
import com.cognizant.cat.dto.StageResponseDTO;

import java.util.List;

public interface IStageService {

    StageResponseDTO create(StageRequestDTO dto);
    List<StageResponseDTO> getAll();
    StageResponseDTO getById(Long id);
    StageResponseDTO update(Long id, StageRequestDTO dto);
    void delete(Long id);
}
