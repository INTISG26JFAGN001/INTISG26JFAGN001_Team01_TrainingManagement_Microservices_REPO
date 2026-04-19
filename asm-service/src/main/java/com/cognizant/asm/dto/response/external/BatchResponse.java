package com.cognizant.asm.dto.response.external;

import lombok.Data;
import java.util.List;

@Data
public class BatchResponse {
    private Long id;
    private Long trainerId;
    private String status;
    private List<Long> courseIds;
    private List<String> courseNames;
}