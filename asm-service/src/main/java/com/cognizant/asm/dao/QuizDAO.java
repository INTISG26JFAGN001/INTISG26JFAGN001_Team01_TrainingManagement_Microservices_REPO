package com.cognizant.asm.dao;

import com.cognizant.asm.entity.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizDAO {

    Optional<Quiz> findById(Long id);
    List<Quiz> findAll();
    Quiz save(Quiz quiz);
    void deleteById(Long id);
}