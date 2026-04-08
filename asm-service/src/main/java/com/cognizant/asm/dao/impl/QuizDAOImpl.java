package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.Quiz;
import com.cognizant.asm.dao.QuizDAO;
import com.cognizant.asm.repository.QuizRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizDAOImpl implements QuizDAO {

    private final QuizRepository quizRepository;

    public QuizDAOImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    @Override
    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public void deleteById(Long id) {
        quizRepository.deleteById(id);
    }
}