package com.cognizant.tms.auth.manager.dao;

import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.repository.IUsersRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<Users> getUsersByFullName(String fullName) {
        return usersRepository.findByFullNameLike("%"+fullName+"%");
    }

    @Override
    @Transactional
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
    @Transactional
    public Users updateUserPasswordHash(Users user) throws UserNotFoundException {
        Users existingUser = this.getUserByUsername(user.getUsername());
        existingUser.setPasswordHash(user.getPasswordHash());
        return usersRepository.save(user);
    }

    @Override
    @Transactional
    public Users deleteUserById(long id) throws UserNotFoundException {
        Optional<Users> existingUser = usersRepository.findById(id);
        if(existingUser.isEmpty()){
            throw new UserNotFoundException("U001", "User with id: "+id+" does not exist");
        }
        try {
            Users userToDelete = existingUser.get();
            // Eagerly initialize the roles collection before deletion
            userToDelete.getRoles().size();
            usersRepository.delete(userToDelete);
            return userToDelete;
        }catch(Exception e){
            System.out.println(e);
            throw new UserNotFoundException("U004", "Failed to delete user with id: "+id);
        }
    }
}
