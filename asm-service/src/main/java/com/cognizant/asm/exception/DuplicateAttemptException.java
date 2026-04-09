package com.cognizant.asm.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DuplicateAttemptException extends RuntimeException {

    public DuplicateAttemptException(String message) {
        super(message);
    }

    public DuplicateAttemptException(Long quizId, Long associateId) {
        super("Associate " + associateId + " has already attempted quiz " + quizId  + ", Only one attempt is allowed per associate");
    }
}
