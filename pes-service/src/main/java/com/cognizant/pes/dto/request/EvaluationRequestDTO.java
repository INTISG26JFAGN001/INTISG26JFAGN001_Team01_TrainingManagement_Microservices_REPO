package com.cognizant.pes.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record EvaluationRequestDTO (
        @NotNull(message = "batch ID is required")
        Long batchId,

        @NotNull(message ="associate id is required")
        Long associateId,

        @Min(value = 0, message = "Score cannot be less than 0")
        @Max(value = 100, message = "Score cannot exceed 100")
        Double interimScore,

        @Min(value=0,message = "Score cannot be less than 0")
        @Max(value=100, message = "Score cannot exceed 100")
        Double finalScore,

        @NotBlank(message = "status must not be blank")
        String overallStatus
){
}
