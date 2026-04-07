package com.cognizant.tms.auth.manager.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SignupResponseDTO {
    private LocalDateTime timestamp;
    private String message;
    private boolean signUpSuccess;

    public SignupResponseDTO() {
    }

    public SignupResponseDTO(LocalDateTime timestamp, String message, boolean loginSuccess) {
        this.timestamp = timestamp;
        this.message = message;
        this.signUpSuccess = loginSuccess;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSignUpSuccess() {
        return signUpSuccess;
    }

    public void setSignUpSuccess(boolean signUpSuccess) {
        this.signUpSuccess = signUpSuccess;
    }

    @Override
    public String toString() {
        return "SignupResponseDTO{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", signUpSuccess=" + signUpSuccess +
                '}';
    }
}
