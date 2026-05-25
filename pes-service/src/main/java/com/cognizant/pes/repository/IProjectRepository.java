package com.cognizant.pes.repository;

import com.cognizant.pes.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByAssociateId(Long associateId);
}
