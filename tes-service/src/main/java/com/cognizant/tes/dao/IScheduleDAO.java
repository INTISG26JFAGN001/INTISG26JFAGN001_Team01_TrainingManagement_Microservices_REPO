package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface IScheduleDAO {
    Schedule save(Schedule schedule);
    Schedule findById(Long scheduleId);
    List<Schedule> findAll();
    Schedule updateSessionDate(Long scheduleId, LocalDateTime sessionDate);
    List<Schedule> findSchedulesByBatchId(Long batchId);

}
