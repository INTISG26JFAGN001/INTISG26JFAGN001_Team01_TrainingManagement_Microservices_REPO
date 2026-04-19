package com.cognizant.asm.dto.response.external;

import lombok.Data;

@Data
public class AssociateResponse {
    private Long id;
    private Long userId;
    private Long courseId;
    private Integer xp;
}