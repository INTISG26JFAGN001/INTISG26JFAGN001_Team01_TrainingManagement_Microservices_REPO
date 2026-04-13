package com.cognizant.pes.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the overall result of an interview evaluation conducted in PES.
 * PES is the owner of all interview evaluation data.
 *
 * An interview evaluation:
 *  - Is linked to a specific assessment (interview) in ASM via assessmentId
 *  - Is for a specific associate via associateId
 *  - Contains an overall score and result status
 *  - Contains rubric-wise criterion scores (see InterviewRubricScore)
 *
 * Why PES owns this? Because interview evaluations involve human evaluators
 * (tech lead, scrum lead) who review the associate and score them per rubric.
 * This is fundamentally a "review/evaluation" activity, which belongs in PES.
 */
@Entity
@Table(name = "interview_evaluations",
        uniqueConstraints = @UniqueConstraint(name = "uk_interview_evaluation",
                columnNames = {"assessment_id", "associate_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the Interview Assessment in ASM (not a FK — cross-service reference)
    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "associate_id", nullable = false)
    private Long associateId;

    @Column(name = "evaluator_id")
    private Long evaluatorId;

    @Column(name = "evaluator_role", length = 50)
    private String evaluatorRole;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "result_status", length = 20)
    private String resultStatus; // "PASS" or "FAIL"

    @Column(name = "evaluator_remarks", columnDefinition = "TEXT")
    private String evaluatorRemarks;

    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;

    // Rubric-wise scores (one row per rubric criterion)
    @OneToMany(mappedBy = "interviewEvaluation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InterviewRubricScore> rubricScores = new ArrayList<>();
}
