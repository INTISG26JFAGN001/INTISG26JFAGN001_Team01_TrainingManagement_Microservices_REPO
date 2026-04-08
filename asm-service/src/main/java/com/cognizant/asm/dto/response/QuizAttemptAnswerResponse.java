package com.cognizant.asm.dto.response;

import com.cognizant.asm.enums.AnswerOption;

import lombok.Data;

@Data
public class QuizAttemptAnswerResponse {

    private Long questionId;
    private String questionText;
    private AnswerOption selectedOption;
    private AnswerOption correctOption;
    private boolean correct;
    private Integer marksAwarded;
}