package com.cognizant.pes.dto;

public record EvaluationResponseDTO(
        Long id,
        Long associateId,
        Double interimScore,
        Double finalScore,
        String overallStatus
) {
}
