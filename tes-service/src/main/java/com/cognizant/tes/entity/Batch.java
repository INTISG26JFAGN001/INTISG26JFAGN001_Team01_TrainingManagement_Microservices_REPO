package com.cognizant.tes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="batch")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="batch_id")
    private Long batchId;

    @Column(name="start_date")
    private LocalDateTime startDate;

    @Column(name="end_date")
    private LocalDateTime endDate;

    @Column(name="trainer_id")
    private Long trainerId;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private BatchStatus status;

    @OneToMany(mappedBy = "batch",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
    // private List<Course> courses;



    public Batch() {}

    public Batch(LocalDateTime startDate,
                 LocalDateTime endDate, Long trainerId, BatchStatus status){

        this.startDate = startDate;
        this.endDate = endDate;
        this.trainerId = trainerId;
        this.status = status;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setBatch(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setBatch(null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Batch batch)) return false;
        return Objects.equals(batchId, batch.batchId)  &&
                Objects.equals(startDate, batch.startDate) &&
                Objects.equals(endDate, batch.endDate) &&
                Objects.equals(trainerId, batch.trainerId) &&
                status == batch.status;
    }


    @Override
    public int hashCode() {
        return Objects.hash(batchId, startDate, endDate, trainerId, status);
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id=" + batchId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", trainerId='" + trainerId + '\'' +
                ", status=" + status +
                '}';
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
}
