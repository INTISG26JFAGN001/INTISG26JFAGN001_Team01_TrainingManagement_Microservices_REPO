package com.cognizant.tms.auth.manager.exception;

public class TokenInvalidException extends RuntimeException{

    public TokenInvalidException(String message){
        super(message);
    }
}
