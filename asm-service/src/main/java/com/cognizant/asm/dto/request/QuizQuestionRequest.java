package com.cognizant.asm.dto.request;

import com.cognizant.asm.enums.AnswerOption;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class QuizQuestionRequest {

    @NotNull(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Option A is required")
    private String optionA;

    @NotNull(message = "Option B is required")
    private String optionB;

    @NotNull(message = "Option C is required")
    private String optionC;

    @NotNull(message = "Option D is required")
    private String optionD;

    @NotNull(message = "Correct option (A/B/C/D) is required")
    private AnswerOption correctOption;

    // defaults to 1 if not provided
    private Integer marks = 1;
}