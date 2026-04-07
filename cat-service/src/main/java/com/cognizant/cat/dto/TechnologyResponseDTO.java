package com.cognizant.cat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnologyResponseDTO {

    private Long id;
    private String name;
}
