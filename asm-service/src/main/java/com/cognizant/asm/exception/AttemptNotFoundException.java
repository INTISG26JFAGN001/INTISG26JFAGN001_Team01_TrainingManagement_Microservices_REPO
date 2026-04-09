package com.cognizant.asm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttemptNotFoundException extends RuntimeException {

    public AttemptNotFoundException(String message) {
        super(message);
    }

    public AttemptNotFoundException(Long quizId, Long associateId) {
        super("No quiz attempt found for quiz " + quizId + " and associate " + associateId);
    }

    public AttemptNotFoundException(Long attemptId) {
        super("Quiz attempt not found with ID: " + attemptId);
    }
}