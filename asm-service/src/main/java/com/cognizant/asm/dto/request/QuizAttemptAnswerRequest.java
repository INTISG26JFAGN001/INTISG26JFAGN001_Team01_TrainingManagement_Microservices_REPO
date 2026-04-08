package com.cognizant.asm.dto.request;

import com.cognizant.asm.enums.AnswerOption;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class QuizAttemptAnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    private AnswerOption selectedOption;
}