package com.cognizant.tes.client;

import com.cognizant.tes.dto.CourseResponseDTO;
import com.cognizant.tes.dto.TechnologyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CAT-SERVICE")
public interface ICourseServiceClient {
    @GetMapping("/courses/{id}")
    CourseResponseDTO getCourseById(@PathVariable("id") Long id);

    @GetMapping("/courses")
    List<CourseResponseDTO> getAllCourses();

    @GetMapping("/technologies/{id}")
    TechnologyResponseDTO getTechnologyById(@PathVariable("id") Long id);

    @GetMapping("/technologies")
    List<TechnologyResponseDTO> getAllTechnologies();
}
