package com.cognizant.cat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponseDTO {

    private Long id;
    private String code;
    private String title;
    private String technologyName;
    private Integer durationDays;
}
