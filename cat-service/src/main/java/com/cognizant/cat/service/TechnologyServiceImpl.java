package com.cognizant.cat.service;

import com.cognizant.cat.dao.TechnologyDAO;
import com.cognizant.cat.dto.TechnologyRequestDTO;
import com.cognizant.cat.dto.TechnologyResponseDTO;
import com.cognizant.cat.entity.Technology;
import com.cognizant.cat.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyServiceImpl implements ITechnologyService {

    private final TechnologyDAO techDAO;

    public TechnologyServiceImpl(TechnologyDAO techDAO) {
        this.techDAO = techDAO;
    }

    public TechnologyResponseDTO create(TechnologyRequestDTO dto) {

        Technology tech = new Technology();
        tech.setName(dto.getName());

        return mapToDTO(techDAO.save(tech));
    }

    public List<TechnologyResponseDTO> getAll() {
        return techDAO.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TechnologyResponseDTO getById(Long id) {
        Technology tech = techDAO.findById(id);

        if (tech == null) {
            throw new ResourceNotFoundException("Technology not found","R001");
        }

        return mapToDTO(tech);
    }

    public TechnologyResponseDTO update(Long id, TechnologyRequestDTO dto) {

        Technology existing = techDAO.findById(id);

        if (existing == null) {
            throw new ResourceNotFoundException("Technology not found","R001");
        }

        existing.setName(dto.getName());

        return mapToDTO(techDAO.save(existing));
    }

    public void delete(Long id) {
        if (techDAO.findById(id) == null) {
            throw new ResourceNotFoundException("Technology not found","R001");
        }
        techDAO.delete(id);
    }

    private TechnologyResponseDTO mapToDTO(Technology tech) {
        return TechnologyResponseDTO.builder()
                .id(tech.getId())
                .name(tech.getName())
                .build();
    }
}
