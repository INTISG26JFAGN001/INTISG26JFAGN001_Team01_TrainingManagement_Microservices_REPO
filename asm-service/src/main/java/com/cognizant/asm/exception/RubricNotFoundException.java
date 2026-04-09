package com.cognizant.asm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RubricNotFoundException extends RuntimeException {

    public RubricNotFoundException(String message) {
        super(message);
    }

    public RubricNotFoundException(Long id) {
        super("Rubric not found with ID: " + id);
    }
}