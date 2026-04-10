package com.cognizant.tes.mapper;

import com.cognizant.tes.dto.ScheduleDTO;
import com.cognizant.tes.entity.Schedule;

public class ScheduleMapper {

    public static ScheduleDTO toDTO(Schedule schedule) {
        if (schedule == null) return null;
        ScheduleDTO dto = new ScheduleDTO();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setBatchId(schedule.getBatchId());
        dto.setSessionDate(schedule.getSessionDate());
        return dto;
    }

    public static Schedule toEntity(ScheduleDTO dto) {
        if (dto == null) return null;
        Schedule schedule = new Schedule();
        schedule.setScheduleId(dto.getScheduleId());
        schedule.setBatchId(dto.getBatchId());
        schedule.setSessionDate(dto.getSessionDate());
        return schedule;
    }
}
