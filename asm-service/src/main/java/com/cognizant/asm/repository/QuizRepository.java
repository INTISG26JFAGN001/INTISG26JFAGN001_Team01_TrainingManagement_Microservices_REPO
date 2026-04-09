package com.cognizant.asm.repository;

import com.cognizant.asm.entity.Quiz;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
