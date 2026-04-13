package com.cognizant.pes.service.impl;

import com.cognizant.pes.client.AsmRubricClient;
import com.cognizant.pes.dao.IInterviewEvaluationDAO;
import com.cognizant.pes.domain.InterviewEvaluation;
import com.cognizant.pes.domain.InterviewRubricScore;
import com.cognizant.pes.dto.request.InterviewEvaluationRequestDTO;
import com.cognizant.pes.dto.response.InterviewEvaluationResponseDTO;
import com.cognizant.pes.dto.response.InterviewEvaluationResponseDTO.RubricScoreResponseDTO;
import com.cognizant.pes.dto.response.RubricResponseDTO;
import com.cognizant.pes.exception.ResourceNotFoundException;
import com.cognizant.pes.service.IInterviewEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for managing interview evaluations in PES.
 *
 * How it works:
 * 1. When an evaluation is submitted, PES calls ASM via Feign (AsmRubricClient)
 *    to validate the rubrics and fetch their weights.
 * 2. PES stores the evaluation with per-rubric scores.
 * 3. When ASM asks for results, PES returns the full evaluation with rubric breakdown.
 *
 * Why in PES? Interview evaluation is a "human review" process. The tech lead or
 * scrum lead assesses the associate's technical and behavioral performance.
 * This is the same domain as project reviews — both are evaluation activities.
 */
@Service
public class InterviewEvaluationService implements IInterviewEvaluationService {

    private final IInterviewEvaluationDAO evaluationDAO;

    private final AsmRubricClient asmRubricClient;

    public InterviewEvaluationService(IInterviewEvaluationDAO evaluationDAO,
                                      AsmRubricClient asmRubricClient) {
        this.evaluationDAO = evaluationDAO;
        this.asmRubricClient = asmRubricClient;
    }

    @Override
    @Transactional
    public InterviewEvaluationResponseDTO submitEvaluation(InterviewEvaluationRequestDTO request) throws ResourceNotFoundException{

        // Step 1: Check for duplicate evaluation
        if (evaluationDAO.existsByAssessmentIdAndAssociateId(request.assessmentId(), request.associateId())) {
            throw new IllegalStateException(
                    "An evaluation already exists for assessmentId=" + request.assessmentId() +
                            " and associateId=" + request.associateId() +
                            ". Use update instead.");
        }

        // Step 2: Fetch rubrics from ASM to validate submitted scores
        List<RubricResponseDTO> rubrics = asmRubricClient.getRubricsForInterview(request.assessmentId());
        Map<Long, RubricResponseDTO> rubricMap = rubrics.stream()
                .collect(Collectors.toMap(RubricResponseDTO::id, r -> r));

        // Step 3: Build rubric score entities
        int totalScore = 0;
        int maxScore = 0;
        List<InterviewRubricScore> rubricScores = new ArrayList<>();

        for (InterviewEvaluationRequestDTO.RubricScoreDTO scoreDTO : request.rubricScores()) {
            RubricResponseDTO rubric = rubricMap.get(scoreDTO.rubricId());
            if (rubric == null) {
                throw new ResourceNotFoundException(
                        "Rubric with ID " + scoreDTO.rubricId() + " not found in ASM for interview " + request.assessmentId());
            }

            // Validate: score awarded cannot exceed rubric weight
            if (scoreDTO.scoreAwarded() > rubric.weight()) {
                throw new IllegalArgumentException(
                        "Score " + scoreDTO.scoreAwarded() + " for rubric '" + rubric.criteria() +
                                "' exceeds maximum allowed weight of " + rubric.weight());
            }

            totalScore += scoreDTO.scoreAwarded();
            maxScore += rubric.weight();

            InterviewRubricScore rubricScore = InterviewRubricScore.builder()
                    .rubricId(rubric.id())
                    .criteria(rubric.criteria())
                    .weight(rubric.weight())
                    .scoreAwarded(scoreDTO.scoreAwarded())
                    .remarks(scoreDTO.remarks())
                    .build();

            rubricScores.add(rubricScore);
        }

        // Step 4: Determine pass/fail
        // Pass if aggregate score >= 60% of max
        String resultStatus = (maxScore > 0 && totalScore * 100 / maxScore >= 60) ? "PASS" : "FAIL";

        // Step 5: Build and save the evaluation
        InterviewEvaluation evaluation = InterviewEvaluation.builder()
                .assessmentId(request.assessmentId())
                .associateId(request.associateId())
                .evaluatorId(request.evaluatorId())
                .evaluatorRole(request.evaluatorRole())
                .evaluatorRemarks(request.evaluatorRemarks())
                .totalScore(totalScore)
                .maxScore(maxScore)
                .resultStatus(resultStatus)
                .evaluatedAt(LocalDateTime.now())
                .rubricScores(rubricScores)
                .build();

        // Link rubric scores back to the evaluation
        rubricScores.forEach(rs -> rs.setInterviewEvaluation(evaluation));

        InterviewEvaluation saved = evaluationDAO.save(evaluation);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewEvaluationResponseDTO getEvaluationByAssessmentAndAssociate(Long assessmentId, Long associateId) throws ResourceNotFoundException {
        InterviewEvaluation evaluation = evaluationDAO
                .findByAssessmentIdAndAssociateId(assessmentId, associateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No interview evaluation found for assessmentId=" + assessmentId +
                                " and associateId=" + associateId));
        return toResponseDTO(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssessment(Long assessmentId) {
        return evaluationDAO.findByAssessmentId(assessmentId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssociate(Long associateId) {
        return evaluationDAO.findByAssociateId(associateId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Mapper ────────────────────────────────────────────────────────────────

    private InterviewEvaluationResponseDTO toResponseDTO(InterviewEvaluation eval) {
        List<RubricScoreResponseDTO> rubricDTOs = eval.getRubricScores().stream()
                .map(rs -> new RubricScoreResponseDTO(
                        rs.getId(),
                        rs.getRubricId(),
                        rs.getCriteria(),
                        rs.getWeight(),
                        rs.getScoreAwarded(),
                        rs.getRemarks()
                ))
                .collect(Collectors.toList());

        return new InterviewEvaluationResponseDTO(
                eval.getId(),
                eval.getAssessmentId(),
                eval.getAssociateId(),
                eval.getEvaluatorId(),
                eval.getEvaluatorRole(),
                eval.getEvaluatorRemarks(),
                eval.getTotalScore(),
                eval.getMaxScore(),
                eval.getResultStatus(),
                eval.getEvaluatedAt(),
                rubricDTOs
        );
    }
}
