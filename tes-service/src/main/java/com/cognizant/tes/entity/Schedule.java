package com.cognizant.tes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schedule_id")
    private Long scheduleId;

    @Column(name="batch_id", nullable = false)
    private Long batchId;

    @Column(name="session_date")
    private LocalDateTime sessionDate;


    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Schedule schedule)) return false;
        return Objects.equals(scheduleId, schedule.scheduleId) && Objects.equals(batchId, schedule.batchId) && Objects.equals(sessionDate, schedule.sessionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, batchId, sessionDate);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", batchId=" + batchId +
                ", sessionDate=" + sessionDate +
                '}';
    }
}
