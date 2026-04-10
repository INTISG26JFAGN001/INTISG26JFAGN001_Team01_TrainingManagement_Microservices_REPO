package com.cognizant.tes.dao;

import com.cognizant.tes.entity.Batch;
import com.cognizant.tes.entity.BatchStatus;
import com.cognizant.tes.exception.InvalidBatchException;
import com.cognizant.tes.repository.IBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BatchDAOImpl implements IBatchDAO{

    private final IBatchRepository batchRepository;

    public BatchDAOImpl(IBatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<Batch> findAll(){
        return batchRepository.findAll();
    }

    public Batch save(Batch batch){
        return batchRepository.save(batch);
    }

    public Batch findById(Long id){
        return batchRepository.findById(id).orElseThrow(()->
                new InvalidBatchException("Batch not found with id "+id)
        );
    }

    public Batch deleteById(Long id){
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new InvalidBatchException("Batch not found with id " + id));

        batchRepository.deleteById(id);

        return batch;
    }

    public List<Batch> findByStatus(BatchStatus status){

        return batchRepository.findByStatus(status);
    }
    public List<Batch> findByCourseId(Long courseId) {

        return batchRepository.findBatchesByCourseId(courseId);
    }

    public List<Batch> findByTrainerId(Long trainer_id){

        return batchRepository.findByTrainerId(trainer_id);
    }

}
