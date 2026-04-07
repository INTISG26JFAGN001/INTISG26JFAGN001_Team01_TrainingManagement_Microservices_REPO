package com.cognizant.tms.auth.manager.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestDTO {
    @NotBlank(message = "${user.dto.username.notblank}")
    private String username;
    @NotBlank(message = "${user.dto.password.notblank}")
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
