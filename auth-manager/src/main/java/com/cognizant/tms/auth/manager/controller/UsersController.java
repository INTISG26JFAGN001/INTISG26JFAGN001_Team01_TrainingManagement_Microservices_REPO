package com.cognizant.tms.auth.manager.controller;

import com.cognizant.tms.auth.manager.dto.UsersDTO;
import com.cognizant.tms.auth.manager.mapper.UserMapper;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    private IUsersService usersService;

    @Autowired
    public UsersController(IUsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsersDTO>> getAllUsers(){
        List<Users> usersList = usersService.getAllUsers();
        List<UsersDTO> usersDTOS = usersList.stream().map(u-> UserMapper.mapToUsersDTO(u)).toList();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getById(@PathVariable long id){
        Users users = usersService.getUserById(id);
        UsersDTO usersDTO = UserMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTO);
    }

    @GetMapping("/username")
    public ResponseEntity<UsersDTO> getByUsername(@RequestParam("key") String username){
        Users users = usersService.getUserByUsername(username);
        UsersDTO usersDTO = UserMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTO);
    }

    @GetMapping("/email")
    public ResponseEntity<UsersDTO> getByEmail(@RequestParam("key") String email){
        Users users = usersService.getUserByEmail(email);
        UsersDTO usersDTO = UserMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTO);
    }

    @GetMapping("/name")
    public ResponseEntity<List<UsersDTO>> getAllUsers(@RequestParam("key") String fullname){
        List<Users> usersList = usersService.getUsersByFullName(fullname);
        List<UsersDTO> usersDTOS = usersList.stream().map(u-> UserMapper.mapToUsersDTO(u)).toList();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsersDTO> deleteUser(@PathVariable long id){
        Users users = usersService.deleteUserById(id);
        UsersDTO usersDTO = UserMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usersDTO);
    }
}
