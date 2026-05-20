package com.cognizant.pes.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_project")
    )
    private Project project;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    private double score;

    private String comments;

    private String type;
}