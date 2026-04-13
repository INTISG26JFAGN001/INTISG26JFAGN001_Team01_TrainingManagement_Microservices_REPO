package com.cognizant.pes.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="project_id")
    private Long projectId;

    @Column(name="reviewer_id")
    private Long reviewerId;

    private double score;

    private String comments;

    private String type;
}
