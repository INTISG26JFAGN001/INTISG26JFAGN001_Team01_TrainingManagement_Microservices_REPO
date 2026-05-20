package com.cognizant.asm.dto.response.external;

import lombok.Data;

@Data
public class AssociateResponse {
    private Long id;
    private Long userId;
    private Long batchId;
    private Integer xp;
}