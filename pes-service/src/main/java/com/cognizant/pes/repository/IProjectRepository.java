package com.cognizant.pes.repository;

import com.cognizant.pes.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectRepository extends JpaRepository<Project,Long> {
}
