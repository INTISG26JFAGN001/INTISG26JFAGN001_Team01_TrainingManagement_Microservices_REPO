package com.cognizant.tes.service;

import com.cognizant.tes.dao.IScheduleDAO;
import com.cognizant.tes.dto.ScheduleDTO;
import com.cognizant.tes.entity.Schedule;
import com.cognizant.tes.mapper.ScheduleMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements IScheduleService {

    private final IScheduleDAO scheduleDAO;

    public ScheduleServiceImpl(IScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public Schedule addSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = ScheduleMapper.toEntity(scheduleDTO);
        return scheduleDAO.save(schedule);
    }

    @Override
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleDAO.findById(scheduleId);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleDAO.findAll();
    }

    @Override
    public Schedule updateSessionDate(Long scheduleId, LocalDateTime sessionDate) {
        return scheduleDAO.updateSessionDate(scheduleId, sessionDate);
    }

    @Override
    public List<Schedule> getSchedulesByBatchId(Long batchId) {
        return scheduleDAO.findSchedulesByBatchId(batchId);
    }
}
