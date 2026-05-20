package com.cognizant.tms.auth.manager.mapper;

import com.cognizant.tms.auth.manager.dto.UsersDTO;
import com.cognizant.tms.auth.manager.exception.RoleNotFoundException;
import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.repository.IRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final IRolesRepository rolesRepository;

    @Autowired
    public UserMapper(IRolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Users mapToUsers(UsersDTO usersDTO){
        return new Users(usersDTO.getId(), usersDTO.getUsername(), usersDTO.getFullName(),
                usersDTO.getEmail(), null, usersDTO.getRoles().stream()
                .map(r-> {
                    if(!r.startsWith("ROLE_")){
                        r = "ROLE_" + r;
                    }
                    Optional<Roles> role = rolesRepository.findByRoleName(r);
                    if(role.isPresent()){
                        return role.get();
                    } else {
                        System.out.println("Role not found: "+r+" for user: "+usersDTO.getUsername());
                        throw new RoleNotFoundException("Role not found: " + r);
                    }
                })
//                .collect(Collectors.toCollection(ArrayList::new)));
                .collect(Collectors.toList()));
    }
    public UsersDTO mapToUsersDTO(Users users){
        return new UsersDTO(users.getId(), users.getUsername(), users.getFullName(), users.getEmail(),
                users.getRoles().stream()
                        .map(r->{
                            if(r.getRoleName() != null){
                                return r.getRoleName();
                            } else {
                                System.out.println("Role name is null for role id: "+r.getId()+" of user: "+users.getUsername());
                                throw new RoleNotFoundException("Role name is null for role id: " + r.getId());
                            }
                        })
//                        .collect(Collectors.toCollection(ArrayList::new)));
                        .collect(Collectors.toList()));
    }

}
