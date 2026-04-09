package com.cognizant.tes.repository;

import com.cognizant.tes.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITrainerRepository extends JpaRepository<Trainer,Long> {
    Trainer findByUserId(Long userId);

    @Query("SELECT t FROM Trainer t JOIN TrainerTechnology tt " +
            "ON t.trainerId = tt.trainerId WHERE tt.technologyId = :technologyId")
    List<Trainer> findTrainersByTechnologyId(@Param("technologyId") Long technologyId);
}
