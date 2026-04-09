package com.cognizant.asm.dto.response;

import lombok.Data;

@Data
public class RubricResponse {

    private Long id;
    private Long assessmentId;
    private String criteria;
    private Integer weight;
}