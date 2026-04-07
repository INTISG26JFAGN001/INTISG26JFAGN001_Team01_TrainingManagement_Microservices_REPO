package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.dao.IRolesDAO;
import com.cognizant.tms.auth.manager.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RolesServiceImpl implements IRolesService{
    private IRolesDAO rolesDAO;

    @Autowired
    public RolesServiceImpl(IRolesDAO rolesDAO){
        this.rolesDAO = rolesDAO;
    }

    @Override
    public Roles getById(long id) {
        return rolesDAO.getById(id);
    }

    @Override
    public List<Roles> getAllRoles() {
        return rolesDAO.getAllRoles();
    }

    @Override
    public Roles create(Roles role) {
        return rolesDAO.create(role);
    }

    @Override
    public Roles getByRoleName(String roleName) {
        return rolesDAO.getByRoleName(roleName);
    }
}
