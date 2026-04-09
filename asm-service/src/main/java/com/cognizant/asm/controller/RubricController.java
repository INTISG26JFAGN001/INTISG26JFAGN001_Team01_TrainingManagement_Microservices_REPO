package com.cognizant.asm.controller;

import com.cognizant.asm.service.RubricService;
import com.cognizant.asm.dto.request.CreateRubricRequest;
import com.cognizant.asm.dto.response.RubricResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessments/{assessmentId}/rubrics")
public class RubricController {

    private RubricService rubricService;

    public RubricController(RubricService rubricService) {
        this.rubricService = rubricService;
    }

    @PostMapping
    public ResponseEntity<RubricResponse> createRubric(@PathVariable Long assessmentId, @Valid @RequestBody CreateRubricRequest request) {
        RubricResponse response = rubricService.createRubric(assessmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RubricResponse>> getRubrics(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(rubricService.getRubricsByAssessment(assessmentId));
    }

    @GetMapping("/total-weight")
    public ResponseEntity<Integer> getTotalWeight(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(rubricService.getTotalWeight(assessmentId));
    }

    @DeleteMapping("{rubricId}")
    public ResponseEntity<Void> deleteRubric(@PathVariable Long assessmentId, @PathVariable Long rubricId) {
        rubricService.deleteRubric(rubricId);
        return ResponseEntity.noContent().build();
    }
}