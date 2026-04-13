package com.cognizant.pes.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="project")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Project {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="batch_id")
    private Long batchId;

    @Column(name="title")
    private String title;

    @Column(name="repo_url")
    private String repoUrl;

    @Column(name="submission_date")
    private Date submissionDate;
}
