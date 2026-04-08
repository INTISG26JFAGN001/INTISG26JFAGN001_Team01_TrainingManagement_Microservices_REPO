package com.cognizant.asm.entity;

import com.cognizant.asm.enums.AnswerOption;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz_attempt_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_option")
    private AnswerOption selectedOption;

    @Column(name = "is_correct")
    private boolean correct;

    @Column(name = "marks_awarded")
    private Integer marksAwarded;
}