package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.QuizAttempt;
import com.cognizant.asm.dao.QuizAttemptDAO;
import com.cognizant.asm.repository.QuizAttemptRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizAttemptDAOImpl implements QuizAttemptDAO {

    private final QuizAttemptRepository quizAttemptRepository;

    public QuizAttemptDAOImpl(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Override
    public Optional<QuizAttempt> findById(Long id) {
        return quizAttemptRepository.findById(id);
    }

    @Override
    public List<QuizAttempt> findAll() {
        return quizAttemptRepository.findAll();
    }

    @Override
    public QuizAttempt save(QuizAttempt attempt) {
        return quizAttemptRepository.save(attempt);
    }

    @Override
    public void deleteById(Long id) {
        quizAttemptRepository.deleteById(id);
    }

    @Override
    public List<QuizAttempt> findByAssociateId(Long associateId) {
        return quizAttemptRepository.findByAssociateId(associateId);
    }

    @Override
    public List<QuizAttempt> findByQuizId(Long quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }

    @Override
    public boolean existsByQuizIdAndAssociateId(Long quizId, Long associateId) {
        return quizAttemptRepository.existsByQuizIdAndAssociateId(quizId, associateId);
    }

    @Override
    public Optional<QuizAttempt> findByQuizIdAndAssociateId(Long quizId, Long associateId) {
        return quizAttemptRepository.findByQuizIdAndAssociateId(quizId, associateId);
    }
}