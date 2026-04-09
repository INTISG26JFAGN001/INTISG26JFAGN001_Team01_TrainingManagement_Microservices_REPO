package com.cognizant.pes.dto;

public record ReviewResponseDTO(
        Long reviewerId,
        Integer score,
        String comments,
        String type

) {
}