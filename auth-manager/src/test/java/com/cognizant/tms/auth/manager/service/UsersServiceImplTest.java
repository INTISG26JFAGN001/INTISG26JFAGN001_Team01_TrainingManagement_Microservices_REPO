package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.dao.IUsersDAO;
import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    private IUsersDAO usersDAO;

    @InjectMocks
    private UsersServiceImpl usersService;

    private Users user;

    @BeforeEach
    void setUp() {
        Roles role = new Roles(1, "ROLE_ADMIN");
        user = new Users(1L, "john", "John Doe", "john@example.com", "hashedPass", List.of(role));
    }

    @Test
    void createUser_shouldReturnTrue() {
        when(usersDAO.createUser(user)).thenReturn(true);
        assertTrue(usersService.createUser(user));
        verify(usersDAO).createUser(user);
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(usersDAO.getUserById(1L)).thenReturn(user);
        assertEquals(user, usersService.getUserById(1L));
    }

    @Test
    void getUserById_shouldThrowWhenNotFound() {
        when(usersDAO.getUserById(99L)).thenThrow(new UserNotFoundException("404", "Not found"));
        assertThrows(UserNotFoundException.class, () -> usersService.getUserById(99L));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(usersDAO.getAllUsers()).thenReturn(List.of(user));
        List<Users> result = usersService.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        when(usersDAO.getUserByEmail("john@example.com")).thenReturn(user);
        assertEquals(user, usersService.getUserByEmail("john@example.com"));
    }

    @Test
    void getUserByUsername_shouldReturnUser() {
        when(usersDAO.getUserByUsername("john")).thenReturn(user);
        assertEquals(user, usersService.getUserByUsername("john"));
    }

    @Test
    void getUsersByFullName_shouldReturnList() {
        when(usersDAO.getUsersByFullName("John Doe")).thenReturn(List.of(user));
        assertEquals(1, usersService.getUsersByFullName("John Doe").size());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        when(usersDAO.updateUser(user)).thenReturn(user);
        assertEquals(user, usersService.updateUser(user));
    }

    @Test
    void deleteUserById_shouldReturnDeletedUser() {
        when(usersDAO.deleteUserById(1L)).thenReturn(user);
        assertEquals(user, usersService.deleteUserById(1L));
    }
}

