package com.cognizant.cat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class StageRequestDTO {

    @NotBlank(message = "Stage name is required")
    private String name;

    @NotNull(message = "Order is required")
    private Integer ord;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
