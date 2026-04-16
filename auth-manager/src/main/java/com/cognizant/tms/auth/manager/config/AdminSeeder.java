package com.cognizant.tms.auth.manager.config;

import com.cognizant.tms.auth.manager.exception.RoleNotFoundException;
import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.service.IRolesService;
import com.cognizant.tms.auth.manager.service.IUsersService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AdminSeeder {
    private final IUsersService usersService;
    private final IRolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminSeeder(IUsersService usersService, IRolesService rolesService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void seedAdmin(){
        Users admin = null;
        List<Roles> roles = new ArrayList<>();
        try{
            admin = usersService.getUserByUsername("admin");
        }catch(UserNotFoundException e){
            admin = new Users();
            admin.setUsername("admin");
            admin.setEmail("admin@cts.com");
            admin.setFullName("ADMIN CTS");
            admin.setPasswordHash(passwordEncoder.encode("pass"));
        }
        try{
            roles = rolesService.getAllRoles();
            if(roles.isEmpty()){
                throw new RoleNotFoundException("No roles found");
            }
        }catch(RoleNotFoundException e){
            rolesService.create(new Roles(0, "ROLE_ADMIN"));
            rolesService.create(new Roles(0, "ROLE_TRAINER"));
            rolesService.create(new Roles(0, "ROLE_ASSOCIATE"));
            rolesService.create(new Roles(0, "ROLE_COACH"));
            rolesService.create(new Roles(0, "ROLE_TECH_LEAD"));
            rolesService.create(new Roles(0, "ROLE_SCRUM_LEAD"));
        }
        Roles adminRole = rolesService.getByRoleName("ROLE_ADMIN");
        admin.setRoles(List.of(adminRole));
        try {
            usersService.createUser(admin);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
