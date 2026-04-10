package com.cognizant.tes.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAssociateDTO {
    private long userId;
    private long courseId;
    private int xp;

    public CreateAssociateDTO() {
    }

    public CreateAssociateDTO(long userId, long courseId, int xp) {
        this.userId = userId;
        this.courseId = courseId;
        this.xp = xp;
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
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", xp=" + xp +
                '}';
    }
}
