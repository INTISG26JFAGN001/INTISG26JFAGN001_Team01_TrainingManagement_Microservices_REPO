package com.cognizant.pes.dto.response;

public record ReviewResponseDTO(
        Long reviewerId,
        Integer score,
        String comments,
        String type

) {
}