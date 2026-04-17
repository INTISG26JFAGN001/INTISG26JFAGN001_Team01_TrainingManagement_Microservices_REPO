package com.cognizant.asm.exception;

 // HTTP 422 Unprocessable Entity is returned, HttpStatus.UNPROCESSABLE_ENTITY) is deprecated in Spring 7

public class RubricTotalNotHundredException extends RuntimeException {

    public  RubricTotalNotHundredException(String message) {
        super(message);
    }

    public RubricTotalNotHundredException(Long interviewId, int currentTotal) {
        super("Interview ID" + interviewId + " cannot be finalized: rubric weights must total exactly 100, " + "but current total is " + currentTotal + ". " +
                "Please add or adjust rubric criteria so that the total weight equals 100.");
    }
}