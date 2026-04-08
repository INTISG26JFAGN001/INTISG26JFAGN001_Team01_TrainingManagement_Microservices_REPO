package com.cognizant.asm.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("INTERVIEW_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResult extends AssessmentResult {

    // Helper for Interview Assessment
    public Interview getInterview() {
        return (Interview) getAssessment();
    }

    public void setInterview(Interview interview) {
        setAssessment(interview);
    }

    @Column(name = "evaluator_id")
    private Long evaluatorId;

    // Reference ID to the full evaluation in the Project & Evaluation Service (PES)
    @Column(name = "external_eval_id")
    private String externalEvalId;

    @Column(name = "panel_comments", columnDefinition = "TEXT")
    private String panelComments;
}