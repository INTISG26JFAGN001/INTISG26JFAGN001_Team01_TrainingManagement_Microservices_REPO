package com.cognizant.asm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RubricWeightExceededException extends RuntimeException {

    public RubricWeightExceededException(String message) {
        super(message);
    }

    public RubricWeightExceededException(int currentTotal, int addingWeight) {
        super("Adding rubric weight " + addingWeight + " would exceed 100, current total weight is " + currentTotal);
    }
}