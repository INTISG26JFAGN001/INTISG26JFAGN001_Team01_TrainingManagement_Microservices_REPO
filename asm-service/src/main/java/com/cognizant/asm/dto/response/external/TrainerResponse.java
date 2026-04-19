package com.cognizant.asm.dto.response.external;

import lombok.Data;
import java.util.List;

@Data
public class TrainerResponse {
    private Long trainerId;
    private Long userId;
    private List<Long> technologyIds;
    private List<String> technologyNames;
}