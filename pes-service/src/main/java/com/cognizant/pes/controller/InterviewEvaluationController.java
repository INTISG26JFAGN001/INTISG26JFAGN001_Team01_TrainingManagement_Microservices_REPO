package com.cognizant.pes.controller;

import com.cognizant.pes.dto.request.InterviewEvaluationRequestDTO;
import com.cognizant.pes.dto.response.InterviewEvaluationResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.service.IInterviewEvaluationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview-evaluations")
public class InterviewEvaluationController {

    @Autowired
    private final IInterviewEvaluationService evaluationService;

    public InterviewEvaluationController(IInterviewEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }


    @PostMapping
    public ResponseEntity<InterviewEvaluationResponseDTO> submitEvaluation(
            @Valid @RequestBody InterviewEvaluationRequestDTO request) throws ResourceNotFoundException{
        InterviewEvaluationResponseDTO response = evaluationService.submitEvaluation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/assessment/{assessmentId}/associate/{associateId}")
    public ResponseEntity<InterviewEvaluationResponseDTO> getEvaluation(
            @PathVariable Long assessmentId,
            @PathVariable Long associateId) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                evaluationService.getEvaluationByAssessmentAndAssociate(assessmentId, associateId));
    }


    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssessment(
            @PathVariable Long assessmentId) {
        return ResponseEntity.ok(evaluationService.getAllEvaluationsByAssessment(assessmentId));
    }


    @GetMapping("/associate/{associateId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssociate(
            @PathVariable Long associateId) {
        return ResponseEntity.ok(evaluationService.getAllEvaluationsByAssociate(associateId));
    }
}
