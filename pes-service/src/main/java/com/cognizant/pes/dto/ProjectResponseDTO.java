package com.cognizant.pes.dto;

import java.time.LocalDateTime;

/**
 * DTO for outgoing project data.
 * Includes system-generated fields like id and submission_date[cite: 51].
 */
public record ProjectResponseDTO(
        Long id,
        Long batchId,
        String title,
        String repoUrl,
        LocalDateTime submissionDate
) {}