package com.cognizant.cat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StageResponseDTO {

    private Long id;
    private String name;
    private Integer ord;
    private String type;
    private String courseTitle;
}
