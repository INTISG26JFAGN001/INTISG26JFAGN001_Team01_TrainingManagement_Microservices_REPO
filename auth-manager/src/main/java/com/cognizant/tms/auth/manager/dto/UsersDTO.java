package com.cognizant.tms.auth.manager.dto;

import com.cognizant.tms.auth.manager.model.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersDTO {
    private long id;
    @NotBlank(message = "${user.dto.username.notblank}")
    private String username;
    @NotBlank(message = "${user.dto.fullname.notblank}")
    private String fullName;
    @NotBlank(message = "${user.dto.email.notblank}")
    @Email(message = "${user.dto.email.invalid}")
    private String email;

    private List<Roles> roles;

    public UsersDTO() {
    }

    public UsersDTO(long id, String username, String fullName, String email, List<Roles> roles) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
}
