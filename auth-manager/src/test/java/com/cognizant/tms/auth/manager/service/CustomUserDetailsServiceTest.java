package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private IUsersService usersService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        Roles role = new Roles(1, "ROLE_USER");
        Users user = new Users(1L, "john", "John Doe", "john@example.com", "hashedPass", List.of(role));
        when(usersService.getUserByUsername("john")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("hashedPass", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_shouldReturnNullWhenUserNotFound() {
        when(usersService.getUserByUsername("unknown")).thenReturn(null);

        UserDetails result = customUserDetailsService.loadUserByUsername("unknown");

        assertNull(result);
    }
}

