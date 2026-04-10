package com.cognizant.tes.dto;

import jakarta.validation.constraints.NotBlank;

public class AssociateDTO {
    private long id;
    private long userId;
    private long courseId;
    private int xp;

    public AssociateDTO() {
    }

    public AssociateDTO(long userId, long courseId, int xp) {
        this.userId = userId;
        this.courseId = courseId;
        this.xp = xp;
    }

    public AssociateDTO(long id, long userId, long courseId, int xp) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    @Override
    public String toString() {
        return "AssociateDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", xp=" + xp +
                '}';
    }
}
