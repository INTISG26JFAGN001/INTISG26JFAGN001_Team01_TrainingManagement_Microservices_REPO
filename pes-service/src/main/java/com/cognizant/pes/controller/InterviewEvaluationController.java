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

/**
 * REST controller for interview evaluation management in PES.
 *
 * Responsibilities:
 * - Evaluators (tech/scrum leads) submit interview evaluations via POST /interview-evaluations
 * - Results are retrieved per assessment+associate (for ASM Feign calls)
 * - All results for an interview assessment can be listed
 */
@RestController
@RequestMapping("/interview-evaluations")
public class InterviewEvaluationController {

    @Autowired
    private final IInterviewEvaluationService evaluationService;

    public InterviewEvaluationController(IInterviewEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * Submit an interview evaluation.
     * PES fetches rubrics from ASM, validates submitted scores, and stores the evaluation.
     *
     * POST /interview-evaluations
     */
    @PostMapping
    public ResponseEntity<InterviewEvaluationResponseDTO> submitEvaluation(
            @Valid @RequestBody InterviewEvaluationRequestDTO request) throws ResourceNotFoundException{
        InterviewEvaluationResponseDTO response = evaluationService.submitEvaluation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get evaluation result for a specific associate in a specific interview.
     * This endpoint is called by ASM's Feign client (PesInterviewResultClient).
     *
     * GET /interview-evaluations/assessment/{assessmentId}/associate/{associateId}
     */
    @GetMapping("/assessment/{assessmentId}/associate/{associateId}")
    public ResponseEntity<InterviewEvaluationResponseDTO> getEvaluation(
            @PathVariable Long assessmentId,
            @PathVariable Long associateId) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                evaluationService.getEvaluationByAssessmentAndAssociate(assessmentId, associateId));
    }

    /**
     * Get all evaluations for a given interview (all associates).
     *
     * GET /interview-evaluations/assessment/{assessmentId}
     */
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssessment(
            @PathVariable Long assessmentId) {
        return ResponseEntity.ok(evaluationService.getAllEvaluationsByAssessment(assessmentId));
    }

    /**
     * Get all interview evaluations for a given associate across all interviews.
     *
     * GET /interview-evaluations/associate/{associateId}
     */
    @GetMapping("/associate/{associateId}")
    public ResponseEntity<List<InterviewEvaluationResponseDTO>> getAllByAssociate(
            @PathVariable Long associateId) {
        return ResponseEntity.ok(evaluationService.getAllEvaluationsByAssociate(associateId));
    }
}
