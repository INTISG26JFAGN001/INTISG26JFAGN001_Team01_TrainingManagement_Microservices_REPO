package com.cognizant.pes.service;

import com.cognizant.pes.dto.request.InterviewEvaluationRequestDTO;
import com.cognizant.pes.dto.response.InterviewEvaluationResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;

import java.util.List;

public interface IInterviewEvaluationService {

    /**
     * Submit a new interview evaluation.
     * PES fetches rubrics from ASM, validates the submitted scores,
     * stores the evaluation, and returns the saved result.
     */
    InterviewEvaluationResponseDTO submitEvaluation(InterviewEvaluationRequestDTO request) throws ResourceNotFoundException;

    /**
     * Get evaluation result for a specific associate in a specific interview.
     * Called by ASM (via Feign) to retrieve the interview result.
     */
    InterviewEvaluationResponseDTO getEvaluationByAssessmentAndAssociate(Long assessmentId, Long associateId) throws ResourceNotFoundException;

    /**
     * Get all evaluations for a given interview assessment (all associates).
     */
    List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssessment(Long assessmentId);

    /**
     * Get all evaluations for a given associate across all interviews.
     */
    List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssociate(Long associateId);
}
