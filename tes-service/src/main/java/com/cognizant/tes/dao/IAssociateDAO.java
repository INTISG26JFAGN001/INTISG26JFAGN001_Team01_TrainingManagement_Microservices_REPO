package com.cognizant.tes.dao;


import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.exception.InvalidAssociateException;

import java.util.List;

public interface IAssociateDAO {
    boolean create(Associate associate) throws InvalidAssociateException;
    Associate  getById(long id) throws InvalidAssociateException;
    List<Associate> getAll();
    Associate getByUserId(long userId) throws InvalidAssociateException;
    List<Associate> getByBatchId(long batchId) throws InvalidAssociateException;
    List<Associate> getByXp(int xp) throws InvalidAssociateException;
    Associate update(Associate associate) throws InvalidAssociateException;
    boolean deleteById(long id) throws InvalidAssociateException;
}
