package com.cognizant.tms.auth.manager.dao;

import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.repository.IRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RolesDAOImpl implements IRolesDAO{
    private IRolesRepository rolesRepository;
    @Autowired
    public RolesDAOImpl(IRolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Override
    public Roles getById(long id){
        return rolesRepository.findById(id).get();
    }

    @Override
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles create(Roles roles) {
        rolesRepository.save(roles);
        return roles;
    }

    @Override
    public Roles getByRoleName(String roleName) {
        Optional<Roles> roles = rolesRepository.findByRoleName(roleName);
        Roles result = null;
        if(roles.isPresent()){
            result = roles.get();
        }
        return result;
    }
}
