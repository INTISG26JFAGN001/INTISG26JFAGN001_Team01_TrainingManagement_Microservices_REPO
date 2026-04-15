package com.cognizant.tes.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"associate_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="enrollment_id")
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name="batch_id")
    private Batch batch;

    @Column(name="associate_id")
    private Long associateId;

    @Enumerated(EnumType.STRING) //yy
    @Column(name="status")
    private EnrollmentStatus status;

    @Column(name="join_date")
    private LocalDate joinDate;

    @PrePersist
    protected void onCreate(){
        this.joinDate = LocalDate.now();
    }


    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Enrollment that)) return false;
        return Objects.equals(enrollmentId, that.enrollmentId)
                && Objects.equals(batch, that.batch) &&
                Objects.equals(associateId, that.associateId) &&
                status == that.status && Objects.equals(joinDate, that.joinDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId, batch, associateId, status, joinDate);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", batch=" + batch +
                ", associateId=" + associateId +
                ", status=" + status +
                ", joinDate=" + joinDate +
                '}';
    }
}
