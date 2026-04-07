package com.cognizant.tms.auth.manager.service;

import com.cognizant.tms.auth.manager.dao.IUsersDAO;
import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements IUsersService{
    private IUsersDAO usersDAO;

    @Autowired
    public UsersServiceImpl(IUsersDAO usersDAO){
        this.usersDAO = usersDAO;
    }

    @Override
    public boolean createUser(Users user) throws Exception {
        return usersDAO.createUser(user);
    }

    @Override
    public Users getUserById(long id) throws UserNotFoundException {
        return usersDAO.getUserById(id);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersDAO.getAllUsers();
    }

    @Override
    public Users getUserByEmail(String email) throws UserNotFoundException {
        return usersDAO.getUserByEmail(email);
    }

    @Override
    public Users getUserByUsername(String username) throws UserNotFoundException {
        return usersDAO.getUserByUsername(username);
    }

    @Override
    public List<Users> getUsersByFullName(String fullName) {
        return usersDAO.getUsersByFullName(fullName);
    }

    @Override
    public Users updateUser(Users user) throws UserNotFoundException {
        return usersDAO.updateUser(user);
    }

    @Override
    public Users deleteUserById(long id) throws UserNotFoundException {
        return usersDAO.deleteUserById(id);
    }
}
