package com.cognizant.tes.service;

import com.cognizant.tes.dao.IAssociateDAO;
import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.exception.InvalidAssociateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssociateServiceImpl implements IAssociateService {
    private final IAssociateDAO associateDAO;

    @Autowired
    public AssociateServiceImpl(IAssociateDAO associateDAO) {
        this.associateDAO = associateDAO;
    }

    @Override
    public boolean create(Associate associate) {
        return associateDAO.create(associate);
    }

    @Override
    public Associate getById(long id) throws InvalidAssociateException {
        return associateDAO.getById(id);
    }

    @Override
    public List<Associate> getAll() {
        return associateDAO.getAll();
    }

    @Override
    public Associate getByUserId(long userId) throws InvalidAssociateException {
        return associateDAO.getByUserId(userId);
    }

    @Override
    public List<Associate> getByBatchId(long batchId) throws InvalidAssociateException {
        return associateDAO.getByBatchId(batchId);
    }

    @Override
    public List<Associate> getByXp(int xp) throws InvalidAssociateException {
        return associateDAO.getByXp(xp);
    }

    @Override
    public Associate update(Associate associate) throws InvalidAssociateException {
        return associateDAO.update(associate);
    }

    @Override
    public boolean deleteById(long id) throws InvalidAssociateException {
        return associateDAO.deleteById(id);
    }
}
