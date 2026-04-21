package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.dao.IRolesDAO;
import com.cognizant.tms.auth.manager.model.Roles;
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
class RolesServiceImplTest {

    @Mock
    private IRolesDAO rolesDAO;

    @InjectMocks
    private RolesServiceImpl rolesService;

    private Roles role;

    @BeforeEach
    void setUp() {
        role = new Roles(1, "ROLE_USER");
    }

    @Test
    void getById_shouldReturnRole() {
        when(rolesDAO.getById(1L)).thenReturn(role);
        assertEquals(role, rolesService.getById(1L));
    }

    @Test
    void getAllRoles_shouldReturnList() {
        when(rolesDAO.getAllRoles()).thenReturn(List.of(role));
        List<Roles> result = rolesService.getAllRoles();
        assertEquals(1, result.size());
        assertEquals(role, result.get(0));
    }

    @Test
    void create_shouldReturnCreatedRole() {
        when(rolesDAO.create(role)).thenReturn(role);
        assertEquals(role, rolesService.create(role));
        verify(rolesDAO).create(role);
    }

    @Test
    void getByRoleName_shouldReturnRole() {
        when(rolesDAO.getByRoleName("ROLE_USER")).thenReturn(role);
        assertEquals(role, rolesService.getByRoleName("ROLE_USER"));
    }
}

