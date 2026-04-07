package com.cognizant.cat.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CourseRequestDTO {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Technology ID is required")
    private Long technologyId;

    @NotNull(message = "Duration is required")
    private Integer durationDays;
}
