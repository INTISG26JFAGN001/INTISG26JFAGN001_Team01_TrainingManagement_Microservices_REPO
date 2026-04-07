package com.cognizant.tms.auth.manager.mapper;

import com.cognizant.tms.auth.manager.dto.UsersDTO;
import com.cognizant.tms.auth.manager.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static Users mapToUsers(UsersDTO usersDTO){
        return new Users(usersDTO.getId(), usersDTO.getUsername(), usersDTO.getFullName(),
                usersDTO.getEmail(), null, usersDTO.getRoles());
    }
    public static UsersDTO mapToUsersDTO(Users users){
        return new UsersDTO(users.getId(), users.getEmail(), users.getFullName(), users.getEmail(), users.getRoles());
    }

}
