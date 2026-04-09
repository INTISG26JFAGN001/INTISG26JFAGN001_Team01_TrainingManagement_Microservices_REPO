package com.cognizant.pes.controller;

import com.cognizant.pes.dto.EvaluationResponseDTO;
import com.cognizant.pes.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    // US-09: View evaluation reports for a batch
    @GetMapping("/batch/{batchId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<EvaluationResponseDTO>> getBatchEvaluations(@PathVariable Long batchId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByBatch(batchId));
    }

    @GetMapping("/batch/{batchId}/associate/{associateId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'COACH')")
    public ResponseEntity<EvaluationResponseDTO> getAssociateEvaluation(
            @PathVariable Long batchId,
            @PathVariable Long associateId) {
        return ResponseEntity.ok(evaluationService.getAssociateEvaluation(batchId, associateId));
    }

    @PostMapping("/batch/{batchId}/calculate")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> runAggregation(@PathVariable Long batchId) {
        evaluationService.calculateBatchPerformance(batchId);
        // KAFKA: Publish EvaluationUpdated event here [cite: 63, 103]
        return ResponseEntity.accepted().build();
    }
}