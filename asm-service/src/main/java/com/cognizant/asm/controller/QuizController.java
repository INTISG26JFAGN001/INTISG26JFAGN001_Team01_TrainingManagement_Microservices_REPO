package com.cognizant.asm.controller;

import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.service.QuizService;
import com.cognizant.asm.dto.request.CreateQuizRequest;
import com.cognizant.asm.dto.request.UpdateAssessmentRequest;
import com.cognizant.asm.dto.request.QuizAttemptRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.QuizAttemptResultResponse;
import com.cognizant.asm.dto.response.QuizDetailResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessments/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizDetailResponse> createQuiz(@Valid @RequestBody CreateQuizRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        QuizDetailResponse response = quizService.createQuiz(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batches/{batchId}/assessments/quiz")
    public ResponseEntity<QuizDetailResponse> createQuizForBatch(@PathVariable Long batchId, @Valid @RequestBody CreateQuizRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        request.setBatchId(batchId);
        QuizDetailResponse response = quizService.createQuiz(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDetailResponse> getQuizById(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizById(quizId));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(quizService.listQuizzesByBatch(batchId));
    }

    @GetMapping("/batch/{batchId}/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndStatus(@PathVariable Long batchId, @PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(quizService.listQuizzesByBatchAndStatus(batchId, status));
    }

    @PatchMapping("/{quizId}")
    public ResponseEntity<QuizDetailResponse> updateQuiz(@PathVariable Long quizId, @Valid @RequestBody UpdateAssessmentRequest request) {
        return ResponseEntity.ok(quizService.updateQuiz(quizId, request));
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{quizId}/attempt")
    public ResponseEntity<QuizAttemptResultResponse> attemptQuiz(@PathVariable Long quizId, @Valid @RequestBody QuizAttemptRequest request) {
        QuizAttemptResultResponse result = quizService.attemptQuiz(quizId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{quizId}/attempts/{associateId}/result")
    public ResponseEntity<QuizAttemptResultResponse> getAttemptResult(@PathVariable Long quizId, @PathVariable Long associateId) {
        return ResponseEntity.ok(quizService.getAttemptResult(quizId, associateId));
    }

    @GetMapping("/{quizId}/attempts")
    public ResponseEntity<List<QuizAttemptResultResponse>> getAllAttempts(@PathVariable Long quizId) {;
        return ResponseEntity.ok(quizService.getAllAttempts(quizId));
    }
}