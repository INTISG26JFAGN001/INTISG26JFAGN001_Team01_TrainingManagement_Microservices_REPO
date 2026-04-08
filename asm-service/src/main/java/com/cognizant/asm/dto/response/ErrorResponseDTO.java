package com.cognizant.asm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
    // only for validation errors (@Valid failures) Key = field name, Value = error message
    private Map<String, String> fieldErrors;
}