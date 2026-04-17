package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.EvaluationRequestDTO;
import com.cognizant.pes.dto.response.EvaluationResponseDTO;
import com.cognizant.pes.service.impl.EvaluationService;
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

    @PostMapping("/submitEvaluation")
    public ResponseEntity<EvaluationResponseDTO> submitEvaluation(
            @RequestBody EvaluationRequestDTO request) {
        EvaluationResponseDTO response = evaluationService.submitEvaluation(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getBatchEvaluations(@PathVariable Long batchId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByBatch(batchId));
    }

    @GetMapping("/batch/{batchId}/associate/{associateId}")
    public ResponseEntity<EvaluationResponseDTO> getAssociateEvaluation(
            @PathVariable Long batchId,
            @PathVariable Long associateId) {
        return ResponseEntity.ok(evaluationService.getAssociateEvaluation(batchId, associateId));
    }

    @PostMapping("/batch/{batchId}/calculate")
    public ResponseEntity<Void> runAggregation(@PathVariable Long batchId) {
        evaluationService.calculateBatchPerformance(batchId);
        return ResponseEntity.accepted().build();
    }
}