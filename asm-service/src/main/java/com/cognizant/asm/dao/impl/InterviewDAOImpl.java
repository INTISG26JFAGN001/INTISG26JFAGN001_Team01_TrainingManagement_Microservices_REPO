package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.Interview;
import com.cognizant.asm.enums.InterviewCategory;
import com.cognizant.asm.dao.InterviewDAO;
import com.cognizant.asm.repository.InterviewRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InterviewDAOImpl implements InterviewDAO {

    private final InterviewRepository interviewRepository;

    public InterviewDAOImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Override
    public Optional<Interview> findById(Long id) {
        return interviewRepository.findById(id);
    }

    @Override
    public List<Interview> findAll() {
        return interviewRepository.findAll();
    }

    @Override
    public Interview save(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public List<Interview> findByBatchId(Long batchId) {
        return interviewRepository.findByBatchId(batchId);
    }

    @Override
    public List<Interview> findByBatchIdAndInterviewCategory(Long batchId, InterviewCategory category) {
        return interviewRepository.findByBatchIdAndInterviewCategory(batchId, category);
    }
}