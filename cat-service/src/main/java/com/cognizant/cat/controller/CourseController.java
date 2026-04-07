package com.cognizant.cat.controller;

import com.cognizant.cat.dto.ApiResponse;
import com.cognizant.cat.dto.CourseRequestDTO;
import com.cognizant.cat.dto.CourseResponseDTO;
import com.cognizant.cat.service.ICourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService service;

    public CourseController(ICourseService service){
        this.service=service;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@Valid @RequestBody CourseRequestDTO dto) {

        CourseResponseDTO response=service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAll() {

        List<CourseResponseDTO> list=service.getAll();
        System.out.println(list);
        return ResponseEntity.ok(service.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id, @RequestBody CourseRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Course deleted successfully");
    }

}
