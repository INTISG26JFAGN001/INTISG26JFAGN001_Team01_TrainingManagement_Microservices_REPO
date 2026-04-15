package com.cognizant.tes.repository;

import com.cognizant.tes.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByBatchId(Long batchId);
}
