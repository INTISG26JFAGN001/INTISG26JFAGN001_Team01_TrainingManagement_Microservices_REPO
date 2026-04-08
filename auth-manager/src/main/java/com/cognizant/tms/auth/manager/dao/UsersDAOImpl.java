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
    public boolean createUser(Users user) throws UserNotFoundException{
        Optional<Users> existingUserWithUsername = usersRepository.findByUsername(user.getUsername());
        if(existingUserWithUsername.isPresent()){
            throw new UserNotFoundException("U002", "User with username: "+user.getUsername()+" already exists");
        }
        Optional<Users> userWithSameEmail = usersRepository.findByEmail(user.getEmail());
        if(userWithSameEmail.isPresent()){
            throw new UserNotFoundException("U003", "User with email: "+user.getEmail()+" already exists");
        }
        usersRepository.save(user);
        return true;
    }

    @Override
    public Users getUserById(long id) throws UserNotFoundException {
        Optional<Users> users = usersRepository.findById(id);
        Users result = null;
        if(users.isPresent()){
            result = users.get();
        }else{
            throw new UserNotFoundException("U001", "User with id: "+id+" does not exist");
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
            throw new UserNotFoundException("U003", "User with email: "+email+" does not exist");
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
            throw new UserNotFoundException("U002", "User with username: "+username+" does not exist");
        }
        return result;
    }

    @Override
    public List<Users> getUsersByFullName(String fullName) {
        return usersRepository.findByFullNameLike(fullName);
    }

    @Override
    public Users updateUser(Users user) throws UserNotFoundException {
        Optional<Users> existingUserWithId = usersRepository.findById(user.getId());
        if(existingUserWithId.isEmpty()){
            throw new UserNotFoundException("U001", "User with id: "+user.getId()+" does not exist");
        }
        Optional<Users> existingUserWithUsername = usersRepository.findByUsername(user.getUsername());
        if(existingUserWithUsername.isPresent() && existingUserWithUsername.get().getId()!=existingUserWithId.get().getId()){
            throw new UserNotFoundException("U002", "User with username: "+user.getUsername()+" already exists");
        }
        Optional<Users> existingUserWithEmail = usersRepository.findByEmail(user.getEmail());
        if(existingUserWithEmail.isPresent()&& existingUserWithEmail.get().getId()!=existingUserWithId.get().getId()){
            throw new UserNotFoundException("U003", "User with email: "+user.getEmail()+" already exists");
        }
        Users newUser = existingUserWithId.get();
        newUser.setUsername(user.getUsername());
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setRoles(user.getRoles());
        return usersRepository.save(newUser);
    }

    @Override
    public Users updateUserPasswordHash(Users user) throws UserNotFoundException {
        Users existingUser = this.getUserByUsername(user.getUsername());
        existingUser.setPasswordHash(user.getPasswordHash());
        return usersRepository.save(user);
    }

    @Override
    public Users deleteUserById(long id) throws UserNotFoundException {
        Users existingUser = this.getUserById(id);
        usersRepository.delete(existingUser);
        return existingUser;
    }
}
