package com.cognizant.tms.auth.manager.dto;

import com.cognizant.tms.auth.manager.model.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignupRequestDTO {
    @NotBlank(message = "${user.dto.username.notblank}")
    private String username;
    @NotBlank(message = "${user.dto.fullname.notblank}")
    private String fullName;
    @NotBlank(message = "${user.dto.email.notblank}")
    @Email(message = "${user.dto.email.invalid}")
    private String email;
    @NotBlank(message = "${user.dto.password.notblank}")
    private String password;
    private List<String> roles;

    public SignupRequestDTO() {
    }

    public SignupRequestDTO(String username, String fullName, String email, String password, List<String> roles) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SignupRequestDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
