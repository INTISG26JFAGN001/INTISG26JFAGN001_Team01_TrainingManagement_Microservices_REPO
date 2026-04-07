package com.cognizant.cat.service;

import com.cognizant.cat.dto.TechnologyRequestDTO;
import com.cognizant.cat.dto.TechnologyResponseDTO;

import java.util.List;

public interface ITechnologyService {

    TechnologyResponseDTO create(TechnologyRequestDTO dto);
    List<TechnologyResponseDTO> getAll();
    TechnologyResponseDTO getById(Long id);
    TechnologyResponseDTO update(Long id, TechnologyRequestDTO dto);
    void delete(Long id);
}
