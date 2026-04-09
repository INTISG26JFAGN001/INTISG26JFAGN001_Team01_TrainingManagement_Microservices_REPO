package com.cognizant.asm.dto.request;

import lombok.Data;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class QuizAttemptRequest {

    @NotNull(message = "Associate ID is required")
    private Long associateId;

    @NotEmpty(message = "At least one answer is required")
    @Valid
    private List<QuizAttemptAnswerRequest> answers;
}