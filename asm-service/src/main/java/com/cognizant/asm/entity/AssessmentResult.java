package com.cognizant.asm.entity;

import com.cognizant.asm.enums.ResultStatus;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_results",
        uniqueConstraints = @UniqueConstraint(name = "uk_assessment_result", columnNames = {"assessment_id", "associate_id"}))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "result_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "associate_id", nullable = false)
    private Long associateId;

    @Column(name = "score")
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false)
    private ResultStatus resultStatus;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "is_submitted", nullable = false)
    private boolean submitted = false;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
}