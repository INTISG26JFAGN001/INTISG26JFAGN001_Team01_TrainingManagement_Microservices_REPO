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
import lombok.extern.slf4j.Slf4j;   // ✅ Lombok logger
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    public InterviewEvaluationResponseDTO submitEvaluation(InterviewEvaluationRequestDTO request) throws ResourceNotFoundException {
        log.info("Submitting interview evaluation for assessmentId={} and associateId={}",
                request.assessmentId(), request.associateId());

        // Step 1: Check for duplicate evaluation
        if (evaluationDAO.existsByAssessmentIdAndAssociateId(request.assessmentId(), request.associateId())) {
            log.warn("Duplicate evaluation detected for assessmentId={} and associateId={}",
                    request.assessmentId(), request.associateId());
            throw new IllegalStateException("Evaluation already exists. Use update instead.");
        }

        // Step 2: Fetch rubrics from ASM
        log.debug("Fetching rubrics from ASM for assessmentId={}", request.assessmentId());
        List<RubricResponseDTO> rubrics = asmRubricClient.getRubricsForInterview(request.assessmentId());
        Map<Long, RubricResponseDTO> rubricMap = rubrics.stream()
                .collect(Collectors.toMap(RubricResponseDTO::id, r -> r));

        // Step 3: Build rubric scores
        int totalScore = 0;
        int maxScore = 0;
        List<InterviewRubricScore> rubricScores = new ArrayList<>();

        for (InterviewEvaluationRequestDTO.RubricScoreDTO scoreDTO : request.rubricScores()) {
            RubricResponseDTO rubric = rubricMap.get(scoreDTO.rubricId());
            if (rubric == null) {
                log.error("Rubric with ID {} not found in ASM for assessmentId={}",
                        scoreDTO.rubricId(), request.assessmentId());
                throw new ResourceNotFoundException("Rubric not found");
            }

            if (scoreDTO.scoreAwarded() > rubric.weight()) {
                log.error("Invalid score {} for rubric '{}' exceeds weight {}",
                        scoreDTO.scoreAwarded(), rubric.criteria(), rubric.weight());
                throw new IllegalArgumentException("Score exceeds rubric weight");
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
            log.debug("Added rubric score for rubricId={} with awarded={}", rubric.id(), scoreDTO.scoreAwarded());
        }

        // Step 4: Determine pass/fail
        String resultStatus = (maxScore > 0 && totalScore * 100 / maxScore >= 60) ? "PASS" : "FAIL";
        log.info("Evaluation result for assessmentId={} and associateId={} is {} (totalScore={}, maxScore={})",
                request.assessmentId(), request.associateId(), resultStatus, totalScore, maxScore);

        // Step 5: Build and save evaluation
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

        rubricScores.forEach(rs -> rs.setInterviewEvaluation(evaluation));

        InterviewEvaluation saved = evaluationDAO.save(evaluation);
        log.info("Interview evaluation saved successfully with id={}", saved.getId());

        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewEvaluationResponseDTO getEvaluationByAssessmentAndAssociate(Long assessmentId, Long associateId) throws ResourceNotFoundException {
        log.info("Fetching evaluation for assessmentId={} and associateId={}", assessmentId, associateId);
        InterviewEvaluation evaluation = evaluationDAO
                .findByAssessmentIdAndAssociateId(assessmentId, associateId)
                .orElseThrow(() -> {
                    log.warn("No evaluation found for assessmentId={} and associateId={}", assessmentId, associateId);
                    return new ResourceNotFoundException("No interview evaluation found");
                });
        return toResponseDTO(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssessment(Long assessmentId) {
        log.info("Fetching all evaluations for assessmentId={}", assessmentId);
        return evaluationDAO.findByAssessmentId(assessmentId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewEvaluationResponseDTO> getAllEvaluationsByAssociate(Long associateId) {
        log.info("Fetching all evaluations for associateId={}", associateId);
        return evaluationDAO.findByAssociateId(associateId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

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
