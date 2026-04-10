package com.cognizant.tes.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="associate")
public class Associate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Column(name="user_id", nullable = false, unique=true)
    private long userId;
    @Column(name="batch_id", nullable = false)
    private long batchId;
    @Column(name="experience_points", columnDefinition = "int default 0")
    private int xp;

    public Associate() {
    }

    public Associate(long id, long userId, long batchId, int xp) {
        this.id = id;
        this.userId = userId;
        this.batchId = batchId;
        this.xp = xp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    @Override
    public String toString() {
        return "Associate{" +
                "id=" + id +
                ", userId=" + userId +
                ", batchId=" + batchId +
                ", xp=" + xp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Associate associate)) return false;
        return id == associate.id && userId == associate.userId && batchId == associate.batchId && xp == associate.xp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, batchId, xp);
    }
}
