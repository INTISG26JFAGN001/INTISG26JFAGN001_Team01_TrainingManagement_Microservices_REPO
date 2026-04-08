package com.cognizant.asm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssessmentNotAvailableException extends RuntimeException {

    public AssessmentNotAvailableException(String message)
    {
        super(message);
    }

    public AssessmentNotAvailableException(Long assessmentId) {
        super("Assessment " + assessmentId + " is not available for submission, It may be DRAFT, CLOSED, or ARCHIVED");
    }
}