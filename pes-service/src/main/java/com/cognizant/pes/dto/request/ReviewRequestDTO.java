package com.cognizant.pes.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(
        @NotNull(message = "Reviewer ID is required")
        Long reviewerId,

        @Min(value = 0, message = "Score cannot be less than 0")
        @Max(value = 100, message = "Score cannot exceed 100")
        Integer score,

        @NotBlank(message = "Comments are required")
        String comments,

        @NotBlank(message = "Review type must be TECH or SCRUM")
        String type // Matches HLD requirement for tech/scrum lead reviews
) {}