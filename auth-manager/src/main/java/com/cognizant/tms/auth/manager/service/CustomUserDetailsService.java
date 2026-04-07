package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private IUsersService usersService;

    @Autowired
    public CustomUserDetailsService(IUsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersService.getUserByUsername(username);
        UserDetails user = null;
        if(users!=null){
            user = new User(
                    username,
                    users.getPasswordHash(),
                    users.getRoles().stream()
                            .map(role->new SimpleGrantedAuthority(role.getRoleName()))
                            .toList()
            );
        }
        return user;
    }
}
