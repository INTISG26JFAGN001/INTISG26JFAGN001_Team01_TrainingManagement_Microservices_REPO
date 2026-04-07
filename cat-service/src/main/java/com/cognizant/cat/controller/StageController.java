package com.cognizant.cat.controller;

import com.cognizant.cat.entity.Stage;
import com.cognizant.cat.service.IStageService;
import com.cognizant.cat.dto.StageRequestDTO;
import com.cognizant.cat.dto.StageResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/stages")
public class StageController {

    private final IStageService service;

    public StageController(IStageService service) {
        this.service=service;
    }

    @PostMapping
    public ResponseEntity<StageResponseDTO> create(
            @Valid @RequestBody StageRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<StageResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StageResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StageResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StageRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Stage deleted successfully");
    }
}
