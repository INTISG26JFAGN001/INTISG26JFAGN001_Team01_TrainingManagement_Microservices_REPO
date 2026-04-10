package com.cognizant.tes.repository;

import com.cognizant.tes.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAssociateRepository extends JpaRepository<Associate, Long> {
    Optional<Associate> findByUserId(long userId);
    List<Associate> findByBatchId(long batchId);
    List<Associate> findByXp(int xp);
}
