package com.cognizant.asm.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CreateRubricRequest {

    @NotBlank(message = "Criteria name is required")
    @Size(max = 255, message = "Criteria must not exceed 255 characters")
    private String criteria;

    @NotNull(message = "Weight is required")
    @Min(value = 1, message = "Weight must be atleast 1")
    @Max(value = 100, message = "Weight must not exceed 100")
    private Integer weight;
}