package com.cognizant.pes.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Stores the per-rubric (per-criterion) score for an interview evaluation.
 * Each row represents how the associate scored on one specific rubric criterion
 * (fetched from ASM at evaluation time).
 *
 * Example:
 *  - rubricId=1, criteria="Technical Skills", weight=40, scoreAwarded=35
 *  - rubricId=2, criteria="Communication",    weight=30, scoreAwarded=25
 *  - rubricId=3, criteria="Problem Solving",  weight=30, scoreAwarded=28
 *  Total: 88/100
 */
@Entity
@Table(name = "interview_rubric_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRubricScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_evaluation_id", nullable = false)
    private InterviewEvaluation interviewEvaluation;

    // Cross-service reference to ASM's rubric ID
    @Column(name = "rubric_id", nullable = false)
    private Long rubricId;

    @Column(name = "criteria", nullable = false, length = 255)
    private String criteria;

    // The weight of this rubric (copied from ASM at evaluation time for immutability)
    @Column(name = "weight", nullable = false)
    private Integer weight;

    // Score awarded by the evaluator for this criterion (0 to weight)
    @Column(name = "score_awarded", nullable = false)
    private Integer scoreAwarded;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
