package com.cognizant.tms.auth.manager.dao;

import com.cognizant.tms.auth.manager.model.Roles;

import java.util.List;

public interface IRolesDAO {
    Roles getById(long id);
    List<Roles> getAllRoles();
    Roles create(Roles role);
    Roles getByRoleName(String roleName);
}
