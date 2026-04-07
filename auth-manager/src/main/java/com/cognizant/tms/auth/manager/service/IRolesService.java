package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.model.Roles;

import java.util.List;

public interface IRolesService {
    Roles getById(long id);
    List<Roles> getAllRoles();
    Roles create(Roles role);
    Roles getByRoleName(String roleName);
}
