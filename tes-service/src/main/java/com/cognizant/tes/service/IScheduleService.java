package com.cognizant.tes.service;

import com.cognizant.tes.dto.ScheduleDTO;
import com.cognizant.tes.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface IScheduleService {
    Schedule addSchedule(ScheduleDTO scheduleDTO);
    Schedule getScheduleById(Long scheduleId);
    List<Schedule> getAllSchedules();
    Schedule updateSessionDate(Long scheduleId, LocalDateTime sessionDate);
    List<Schedule> getSchedulesByBatchId(Long batchId);
}
