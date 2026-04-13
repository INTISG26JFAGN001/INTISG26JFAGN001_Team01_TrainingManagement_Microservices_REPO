package com.cognizant.asm.controller;

import com.cognizant.asm.enums.AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.service.AssessmentService;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping
    public ResponseEntity<List<AssessmentSummaryResponse>> listAll() {
        return ResponseEntity.ok(assessmentService.listAll());
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(assessmentService.listByBatch(batchId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByType(@PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentService.listByType(type));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByStatus(@PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(assessmentService.listByStatus(status));
    }

    @GetMapping("/batch/{batchId}/type/{type}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndType(@PathVariable Long batchId, @PathVariable AssessmentType type) {
        return ResponseEntity.ok(assessmentService.listByBatchAndType(batchId, type));
    }

    @GetMapping("/batch/{batchId}/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndStatus(@PathVariable Long batchId, @PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(assessmentService.listByBatchAndStatus(batchId, status));
    }

    @PatchMapping("/{assessmentId}")
    public ResponseEntity<AssessmentSummaryResponse> updateAssessment(@PathVariable Long assessmentId, @Valid @RequestBody UpdateAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.updateAssessment(assessmentId, request));
    }

    @DeleteMapping("/assessmentId")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
        return ResponseEntity.noContent().build();
    }
}