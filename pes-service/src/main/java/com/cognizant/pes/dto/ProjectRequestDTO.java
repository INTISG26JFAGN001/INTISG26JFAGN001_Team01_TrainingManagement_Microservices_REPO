package com.cognizant.pes.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for incoming project submission requests.
 * Maps to the Project entity fields: title, batch_id, and repo_url[cite: 51].
 */
public record ProjectRequestDTO(
        @NotBlank(message = "Project title is required")
        String title,

        @NotNull(message = "Batch ID is required")
        Long batchId,

        @NotBlank(message = "Repository URL is required")
        @Pattern(
                regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                message = "Must be a valid URL"
        )
        String repoUrl
) {}