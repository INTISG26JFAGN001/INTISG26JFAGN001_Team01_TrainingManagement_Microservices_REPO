package com.cognizant.cat.service;

import com.cognizant.cat.dto.CourseRequestDTO;
import com.cognizant.cat.dto.CourseResponseDTO;

import java.util.List;

public interface ICourseService {
    CourseResponseDTO create(CourseRequestDTO dto);
    CourseResponseDTO getById(Long id);
    CourseResponseDTO update(Long id, CourseRequestDTO dto);
    void delete(Long id);
    List<CourseResponseDTO> getAll();
}
