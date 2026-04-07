package com.cognizant.tms.auth.manager.dao;

import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersDAOImpl implements IUsersDAO{
    private IUsersRepository usersRepository;

    @Autowired
    public UsersDAOImpl(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public boolean createUser(Users user) throws Exception{
        try {
            usersRepository.save(user);
            return true;
        }catch(Exception e){
            throw new Exception("User could not be created");
        }
    }

    @Override
    public Users getUserById(long id) throws UserNotFoundException {
        Optional<Users> users = usersRepository.findById(id);
        Users result = null;
        if(users.isPresent()){
            result = users.get();
        }else{
            throw new UserNotFoundException("S500", "User with id: "+id+" does not exist");
        }
        return result;
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserByEmail(String email) throws UserNotFoundException {
        Optional<Users> users = usersRepository.findByEmail(email);
        Users result = null;
        if(users.isPresent()){
            result = users.get();
        }else{
            throw new UserNotFoundException("S500", "User with email: "+email+" does not exist");
        }
        return result;
    }

    @Override
    public Users getUserByUsername(String username) throws UserNotFoundException {
        Optional<Users> users = usersRepository.findByUsername(username);
        Users result = null;
        if(users.isPresent()){
            result = users.get();
        }else{
            throw new UserNotFoundException("S500", "User with username: "+username+" does not exist");
        }
        return result;
    }

    @Override
    public List<Users> getUsersByFullName(String fullName) {
        return usersRepository.findByFullNameLike(fullName);
    }

    @Override
    public Users updateUser(Users user) throws UserNotFoundException {
        Users existingUser = this.getUserByUsername(user.getUsername());
        return usersRepository.save(user);
    }

    @Override
    public Users deleteUserById(long id) throws UserNotFoundException {
        Users existingUser = this.getUserById(id);
        usersRepository.delete(existingUser);
        return existingUser;
    }
}
