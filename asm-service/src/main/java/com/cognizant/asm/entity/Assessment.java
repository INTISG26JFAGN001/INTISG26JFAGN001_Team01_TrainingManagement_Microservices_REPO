package com.cognizant.asm.entity;

import com.cognizant.asm.enums. AssessmentType;
import com.cognizant.asm.enums.AssessmentStatus;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assessments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "assessment_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=200)
    private String title;

    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    @Column(name = "stage_id")
    private Long stageId;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "max_score")
    private Integer maxScore;

    @Column(name = "created_by")
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssessmentStatus status = AssessmentStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", insertable = false, updatable = false)
    private AssessmentType type;
}