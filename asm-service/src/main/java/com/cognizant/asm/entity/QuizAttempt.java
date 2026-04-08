package com.cognizant.asm.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("QUIZ_ATTEMPT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt extends AssessmentResult {

    // Helper for Quiz Assessment
    public Quiz getQuiz() {
        return (Quiz) getAssessment();
    }

    public void setQuiz(Quiz quiz) {
        setAssessment(quiz);
    }

    @Column(name = "duration_minutes_taken")
    private Integer durationMinutesTaken;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttemptAnswer> answers = new ArrayList<>();
}