package com.cognizant.tms.auth.manager.repository;

import com.cognizant.tms.auth.manager.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
    List<Users> findByFullNameLike(String fullName);
}
