package com.cognizant.tms.auth.manager.repository;

import com.cognizant.tms.auth.manager.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(String roleName);
}
