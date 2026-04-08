package com.cognizant.cat.service;

import com.cognizant.cat.dao.CourseDAO;
import com.cognizant.cat.dao.StageDAO;
import com.cognizant.cat.dto.StageRequestDTO;
import com.cognizant.cat.dto.StageResponseDTO;
import com.cognizant.cat.entity.Course;
import com.cognizant.cat.entity.Stage;
import com.cognizant.cat.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StageServiceImpl implements IStageService {

    private final StageDAO stageDAO;
    private final CourseDAO courseDAO;

    public StageServiceImpl(StageDAO stageDAO, CourseDAO courseDAO) {
        this.stageDAO = stageDAO;
        this.courseDAO = courseDAO;
    }

    public StageResponseDTO create(StageRequestDTO dto) {

        Course course = courseDAO.findById(dto.getCourseId());

        if (course == null) {
            throw new ResourceNotFoundException("Course not found","R001");
        }

        Stage stage = new Stage();
        stage.setName(dto.getName());
        stage.setOrd(dto.getOrd());
        stage.setType(dto.getType());
        stage.setCourse(course);

        return mapToDTO(stageDAO.save(stage));
    }

    public List<StageResponseDTO> getAll() {
        return stageDAO.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StageResponseDTO getById(Long id) {
        Stage stage = stageDAO.findById(id);

        if (stage == null) {
            throw new ResourceNotFoundException("Stage not found","R001");
        }

        return mapToDTO(stage);
    }

    public StageResponseDTO update(Long id, StageRequestDTO dto) {

        Stage existing = stageDAO.findById(id);

        if (existing == null) {
            throw new ResourceNotFoundException("Stage not found","R001");
        }

        Course course = courseDAO.findById(dto.getCourseId());

        if (course == null) {
            throw new ResourceNotFoundException("Course not found","R001");
        }

        existing.setName(dto.getName());
        existing.setOrd(dto.getOrd());
        existing.setType(dto.getType());
        existing.setCourse(course);

        return mapToDTO(stageDAO.save(existing));
    }

    public void delete(Long id) {
        if (stageDAO.findById(id) == null) {
            throw new ResourceNotFoundException("Stage not found","R001");
        }
        stageDAO.delete(id);
    }

    private StageResponseDTO mapToDTO(Stage stage) {
        return StageResponseDTO.builder()
                .id(stage.getId())
                .name(stage.getName())
                .ord(stage.getOrd())
                .type(stage.getType())
                .courseTitle(stage.getCourse().getTitle())
                .build();
    }
}
