package com.cognizant.pes.dto.response;


public record RubricResponseDTO(
        Long id,
        Long assessmentId,
        String criteria,
        Integer weight
) {}
