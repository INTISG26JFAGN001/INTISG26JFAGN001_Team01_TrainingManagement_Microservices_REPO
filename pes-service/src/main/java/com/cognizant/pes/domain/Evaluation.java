package com.cognizant.pes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="evaluation")
public class Evaluation {
    @Id
    private Long id;

    @Column(name="batch_id")
    private Long batchId;

    @Column(name="associate_id")
    private Long associateId;

    @Column(name="interim_score")
    private double interimScore;

    @Column(name="final_score")
    private double finalScore;

    @Column(name="overall_status")
    private String overallStatus;

}
