package com.cognizant.tes.service;

import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.exception.InvalidAssociateException;

import java.util.List;

public interface IAssociateService {
    boolean create(Associate associate);
    Associate  getById(long id) throws InvalidAssociateException;
    List<Associate> getAll();
    Associate getByUserId(long userId) throws InvalidAssociateException;
    List<Associate> getByBatchId(long batchId) throws InvalidAssociateException;
    List<Associate> getByXp(int xp) throws InvalidAssociateException;
    Associate update(Associate associate) throws InvalidAssociateException;
    boolean deleteById(long id) throws InvalidAssociateException;
}
