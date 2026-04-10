package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Associate;
import com.cognizant.tes.exception.InvalidAssociateException;
import com.cognizant.tes.repository.IAssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AssociateDAOImpl implements IAssociateDAO {
    private final IAssociateRepository associateRepository;

    @Autowired
    public AssociateDAOImpl(IAssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    @Override
    public boolean create(Associate associate) throws InvalidAssociateException {
        Optional<Associate> existingAssociate = associateRepository.findByUserId(associate.getUserId());
        if(existingAssociate.isEmpty()){
            associateRepository.save(associate);
            return true;
        }
        throw new InvalidAssociateException("Associate with user id: " + associate.getUserId() + " already exists");
    }

    @Override
    public Associate getById(long id) throws InvalidAssociateException {
         Optional<Associate> associate = associateRepository.findById(id);
         Associate result = null;
         if(associate.isPresent()){
             result = associate.get();
         }else{
             throw new InvalidAssociateException("Associate with id: "+id+" does not exist");
         }
         return result;
    }

    @Override
    public List<Associate> getAll() {
        return associateRepository.findAll();
    }

    @Override
    public Associate getByUserId(long userId) throws InvalidAssociateException {
        Optional<Associate> associate = associateRepository.findByUserId(userId);
        Associate result = null;
        if(associate.isPresent()){
            result = associate.get();
        }else{
            throw new InvalidAssociateException("Associate with user id: "+userId+" does not exist");
        }
        return result;
    }

    @Override
    public List<Associate> getByBatchId(long batchId) throws InvalidAssociateException {
        List<Associate> associates = associateRepository.findByBatchId(batchId);
        if(associates.isEmpty()){
            throw new InvalidAssociateException("No associates found for course id: "+ batchId);
        }
        return associates;
    }

    @Override
    public List<Associate> getByXp(int xp) throws InvalidAssociateException {
        List<Associate> associates = associateRepository.findByXp(xp);
        if(associates.isEmpty()){
            throw new InvalidAssociateException("No associates found with xp: "+xp);
        }
        return associates;
    }

    @Override
    public Associate update(Associate associate) {
        Optional<Associate> existingAssociate = associateRepository.findById(associate.getId());
        if(existingAssociate.isPresent()){
            Associate updatedAssociate = existingAssociate.get();
            updatedAssociate.setUserId(associate.getUserId());
            updatedAssociate.setBatchId(associate.getBatchId());
            updatedAssociate.setXp(associate.getXp());
            associateRepository.save(updatedAssociate);
            return updatedAssociate;
        }else{
            throw new InvalidAssociateException("Associate with id: "+associate.getId()+" does not exist");
        }
    }

    @Override
    public boolean deleteById(long id) throws InvalidAssociateException {
        Optional<Associate> existingAssociate = associateRepository.findById(id);
        if(existingAssociate.isPresent()){
            associateRepository.delete(existingAssociate.get());
            return true;
        }else{
            throw new InvalidAssociateException("Associate with id: "+id+" does not exist");
        }
    }
}
