package com.cognizant.asm.dto.response;

import lombok.Data;

@Data
public class QuizQuestionResponse {

    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer marks;
}