package com.cognizant.cat.service;

import com.cognizant.cat.dao.CourseDAO;
import com.cognizant.cat.dao.TechnologyDAO;
import com.cognizant.cat.dto.CourseRequestDTO;
import com.cognizant.cat.dto.CourseResponseDTO;
import com.cognizant.cat.entity.Course;
import com.cognizant.cat.entity.Technology;
import com.cognizant.cat.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService{

    private final CourseDAO courseDAO;
    private final TechnologyDAO techDAO;

    public CourseServiceImpl(CourseDAO courseDAO, TechnologyDAO techDAO) {
        this.courseDAO = courseDAO;
        this.techDAO=techDAO;
    }

    public CourseResponseDTO create(CourseRequestDTO dto) {

        Technology tech = techDAO.findById(dto.getTechnologyId());

        if(tech==null) {
            throw new ResourceNotFoundException("Technology not found", "R001");
        }

        Course course = new Course();
        course.setCode(dto.getCode());
        course.setTitle(dto.getTitle());
        course.setTechnology(tech);
        course.setDurationDays(dto.getDurationDays());

        Course saved = courseDAO.save(course);

        return mapToDTO(saved);
    }

    public CourseResponseDTO getById(Long id) {

        Course course=courseDAO.findById(id);

        if(course == null) {
            throw new ResourceNotFoundException("Course not found", "R001");
        }

        return mapToDTO(course);
    }

    public CourseResponseDTO update(Long id, CourseRequestDTO dto) {

        Course existing = courseDAO.findById(id);

        if(existing == null) {
            throw new ResourceNotFoundException("Course not found","R001");
        }

        Technology tech= techDAO.findById(dto.getTechnologyId());

        if(tech == null) {
            throw new ResourceNotFoundException("Technology not found","R001");
        }

        existing.setCode(dto.getCode());
        existing.setTitle(dto.getTitle());
        existing.setTechnology(tech);
        existing.setDurationDays(dto.getDurationDays());

        return mapToDTO(courseDAO.save(existing));
    }

    public void delete(Long id) {

        Course course= courseDAO.findById(id);

        if(course == null) {
            throw new ResourceNotFoundException("Course not found","R001");
        }

        courseDAO.delete(id);
    }

    public List<CourseResponseDTO> getAll() {
        return courseDAO.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CourseResponseDTO mapToDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .code(course.getCode())
                .title(course.getTitle())
                .technologyName(course.getTechnology().getName())
                .durationDays(course.getDurationDays())
                .build();
    }
}