package com.cognizant.cat.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String errorCode;

    public ResourceNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode=errorCode;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
