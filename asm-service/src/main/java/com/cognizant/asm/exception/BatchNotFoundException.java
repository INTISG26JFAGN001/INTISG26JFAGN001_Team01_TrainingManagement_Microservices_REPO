package com.cognizant.asm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BatchNotFoundException extends RuntimeException {

    public BatchNotFoundException(String message) {
        super(message);
    }

    public BatchNotFoundException(Long batchId) {
        super("Batch not found in Training Execution Service with ID: " + batchId);
    }
}