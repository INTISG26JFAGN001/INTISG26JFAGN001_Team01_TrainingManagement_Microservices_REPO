package com.cognizant.asm.service;

import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.dto.request.CreateQuizRequest;
import com.cognizant.asm.dto.request.QuizAttemptRequest;
import com.cognizant.asm.dto.response.QuizDetailResponse;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.QuizAttemptResultResponse;

import java.util.List;

public interface QuizService {

    QuizDetailResponse createQuiz(CreateQuizRequest request, Long createdBy);
    QuizDetailResponse getQuizById(Long quizId);
    List<AssessmentSummaryResponse> listQuizzesByBatch(Long batchId);
    List<AssessmentSummaryResponse> listQuizzesByBatchAndStatus(Long batchId, AssessmentStatus status);
    QuizAttemptResultResponse attemptQuiz(Long quizId, QuizAttemptRequest request);
    QuizAttemptResultResponse getAttemptResult(Long quizId, Long associateId);
    List<QuizAttemptResultResponse> getAllAttempts(Long quizId);
}