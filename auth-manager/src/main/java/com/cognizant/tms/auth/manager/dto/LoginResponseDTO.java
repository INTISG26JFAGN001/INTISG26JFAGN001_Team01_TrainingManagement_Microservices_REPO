package com.cognizant.tms.auth.manager.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoginResponseDTO {
    private LocalDateTime timestamp;
    private String message;
    private boolean loginSuccess;
    private String accessToken;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(LocalDateTime timestamp, String message, boolean loginSuccess, String accessToken) {
        this.timestamp = timestamp;
        this.message = message;
        this.loginSuccess = loginSuccess;
        this.accessToken = accessToken;
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

    public boolean getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", loginSuccess=" + loginSuccess +
                ", accessToken=" + accessToken +
                '}';
    }
}
