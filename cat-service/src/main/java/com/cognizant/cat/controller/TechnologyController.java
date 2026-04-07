package com.cognizant.cat.controller;

import com.cognizant.cat.entity.Technology;
import com.cognizant.cat.service.ITechnologyService;
import com.cognizant.cat.dto.TechnologyRequestDTO;
import com.cognizant.cat.dto.TechnologyResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/technologies")
public class TechnologyController {

    private final ITechnologyService service;

    public TechnologyController(ITechnologyService service){
        this.service=service;
    }

    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> create(
            @Valid @RequestBody TechnologyRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TechnologyRequestDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Technology deleted successfully");
    }
}
