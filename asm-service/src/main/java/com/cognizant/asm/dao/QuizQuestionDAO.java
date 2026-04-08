package com.cognizant.asm.dao;

import com.cognizant.asm.entity.QuizQuestion;

import java.util.List;
import java.util.Optional;

public interface QuizQuestionDAO {

    Optional<QuizQuestion> findById(Long id);
    List<QuizQuestion> findAll();
    QuizQuestion save(QuizQuestion question);
    void deleteById(Long id);

    List<QuizQuestion> findByQuizIdOrderByIdAsc(Long quizId);
    long countByQuizId(Long quizId);
}