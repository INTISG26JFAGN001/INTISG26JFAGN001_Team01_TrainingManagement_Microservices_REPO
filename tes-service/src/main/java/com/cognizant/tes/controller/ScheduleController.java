package com.cognizant.tes.controller;

import com.cognizant.tes.dto.ScheduleDTO;
import com.cognizant.tes.entity.Schedule;
import com.cognizant.tes.mapper.ScheduleMapper;
import com.cognizant.tes.service.IScheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final IScheduleService scheduleService;

    public ScheduleController(IScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.addSchedule(scheduleDTO);
        return ScheduleMapper.toDTO(schedule);
    }

    @GetMapping("/{scheduleId}")
    public ScheduleDTO getScheduleById(@PathVariable("id") Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return ScheduleMapper.toDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream().map(ScheduleMapper::toDTO).toList();
    }

    @PutMapping("/{scheduleId}/session-date")
    public ScheduleDTO updateSessionDate(@PathVariable("id") Long scheduleId,
                                         @RequestParam LocalDateTime sessionDate) {
        Schedule schedule = scheduleService.updateSessionDate(scheduleId, sessionDate);
        return ScheduleMapper.toDTO(schedule);
    }

    @GetMapping("/batch")
    public List<ScheduleDTO> getSchedulesByBatchId(@RequestParam("id") Long batchId) {
        List<Schedule> schedules = scheduleService.getSchedulesByBatchId(batchId);
        return schedules.stream().map(ScheduleMapper::toDTO).toList();
    }
}
