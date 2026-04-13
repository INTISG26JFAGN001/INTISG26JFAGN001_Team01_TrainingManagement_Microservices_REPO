package com.cognizant.pes.dto.response;

import java.time.LocalDateTime;


public record ProjectResponseDTO(
        Long id,
        Long batchId,
        String title,
        String repoUrl,
        LocalDateTime submissionDate
) {}