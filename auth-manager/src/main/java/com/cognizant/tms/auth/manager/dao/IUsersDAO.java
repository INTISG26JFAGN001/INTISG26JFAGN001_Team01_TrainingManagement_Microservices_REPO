package com.cognizant.tms.auth.manager.dao;

import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Users;

import java.util.List;

public interface IUsersDAO {
    boolean createUser(Users user) throws Exception;
    Users getUserById(long id) throws UserNotFoundException;
    List<Users> getAllUsers();
    Users getUserByEmail(String email) throws UserNotFoundException;
    Users getUserByUsername(String username) throws UserNotFoundException;
    List<Users> getUsersByFullName(String fullName);
    Users updateUser(Users user) throws UserNotFoundException;
    Users deleteUserById(long id) throws UserNotFoundException;
}
