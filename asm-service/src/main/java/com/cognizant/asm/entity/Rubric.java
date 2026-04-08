package com.cognizant.asm.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "rubrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rubric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "criteria", nullable = false, length = 255)
    private String criteria;

    @Column(name = "weight", nullable = false)
    private Integer weight;
}