package com.cognizant.tes.dto;

public class CreateAssociateDTO {
    private long userId;
    private long batchid;
    private int xp;

    public CreateAssociateDTO() {
    }

    public CreateAssociateDTO(long userId, long batchid, int xp) {
        this.userId = userId;
        this.batchid = batchid;
        this.xp = xp;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBatchid() {
        return batchid;
    }

    public void setBatchid(long batchid) {
        this.batchid = batchid;
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
                ", batchid=" + batchid +
                ", xp=" + xp +
                '}';
    }
}
