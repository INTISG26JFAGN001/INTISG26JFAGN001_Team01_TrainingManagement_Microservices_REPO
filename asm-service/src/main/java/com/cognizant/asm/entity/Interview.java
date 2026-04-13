package com.cognizant.asm.entity;

import com.cognizant.asm.enums.InterviewCategory;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("INTERVIEW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interview extends Assessment {

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_category")
    private InterviewCategory interviewCategory;

    @Column(name = "scheduled_date_time")
    private LocalDateTime scheduledDateTime;

    @Column(name = "evaluator_role", length = 50)
    private String evaluatorRole;
}