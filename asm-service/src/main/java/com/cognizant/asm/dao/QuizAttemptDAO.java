package com.cognizant.asm.dao;

import com.cognizant.asm.entity.QuizAttempt;

import java.util.List;
import java.util.Optional;

public interface QuizAttemptDAO {

    Optional<QuizAttempt> findById(Long id);
    List<QuizAttempt> findAll();
    QuizAttempt save(QuizAttempt attempt);
    void deleteById(Long id);

    List<QuizAttempt> findByAssociateId(Long associateId);
    List<QuizAttempt> findByQuizId(Long quizId);
    boolean existsByQuizIdAndAssociateId(Long quizId, Long associateId);
    Optional<QuizAttempt> findByQuizIdAndAssociateId(Long quizId, Long associateId);
}