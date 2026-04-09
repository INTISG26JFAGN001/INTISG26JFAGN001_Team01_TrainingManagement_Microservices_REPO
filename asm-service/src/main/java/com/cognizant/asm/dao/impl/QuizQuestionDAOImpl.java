package com.cognizant.asm.dao.impl;

import com.cognizant.asm.entity.QuizQuestion;
import com.cognizant.asm.dao.QuizQuestionDAO;
import com.cognizant.asm.repository.QuizQuestionRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuizQuestionDAOImpl implements QuizQuestionDAO {

    private final QuizQuestionRepository quizQuestionRepository;

    public QuizQuestionDAOImpl(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Override
    public Optional<QuizQuestion> findById(Long id) {
        return quizQuestionRepository.findById(id);
    }

    @Override
    public List<QuizQuestion> findAll() {
        return quizQuestionRepository.findAll();
    }

    @Override
    public QuizQuestion save(QuizQuestion question) {
        return quizQuestionRepository.save(question);
    }

    @Override
    public void deleteById(Long id) {
        quizQuestionRepository.deleteById(id);
    }

    @Override
    public List<QuizQuestion> findByQuizIdOrderByIdAsc(Long quizId) {
        return quizQuestionRepository.findByQuizIdOrderByIdAsc(quizId);
    }

    @Override
    public long countByQuizId(Long quizId) {
        return quizQuestionRepository.countByQuizId(quizId);
    }
}