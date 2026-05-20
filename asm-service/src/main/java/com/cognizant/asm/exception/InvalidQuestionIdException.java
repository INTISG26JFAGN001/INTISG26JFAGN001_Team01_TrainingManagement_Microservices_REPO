package com.cognizant.asm.exception;

import java.util.List;

public class InvalidQuestionIdException extends RuntimeException {

    public InvalidQuestionIdException(Long questionId, Long quizId, List<Long> validIds) {
        super(String.format(
            "Question ID %d does not belong to Quiz %d. " +
            "Valid question IDs for this quiz are: %s. " +
            "Tip: call GET /assessments/quiz/%d to see all questions with their IDs.",
            questionId, quizId, validIds, quizId
        ));
    }
}
