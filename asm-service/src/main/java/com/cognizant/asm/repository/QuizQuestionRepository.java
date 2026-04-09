package com.cognizant.asm.repository;

import com.cognizant.asm.entity.QuizQuestion;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByQuizIdOrderByIdAsc(Long quizId);
    long countByQuizId(Long quizId);
}
