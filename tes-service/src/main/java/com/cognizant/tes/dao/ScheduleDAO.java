package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Schedule;
import com.cognizant.tes.exception.InvalidScheduleException;
import com.cognizant.tes.repository.IScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ScheduleDAO implements IScheduleDAO{
    private final IScheduleRepository scheduleRepository;

    public ScheduleDAO(IScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new InvalidScheduleException("Schedule not found with id " + scheduleId));
    }


    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule updateSessionDate(Long scheduleId, LocalDateTime sessionDate) {
        Schedule schedule = findById(scheduleId);
        schedule.setSessionDate(sessionDate);
        return scheduleRepository.save(schedule);
    }


    public List<Schedule> findSchedulesByBatchId(Long batchId) {
        return scheduleRepository.findByBatch_BatchId(batchId);
    }
}
