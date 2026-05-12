package com.cognizant.asm.controller;

import com.cognizant.asm.enums.AssessmentStatus;
import com.cognizant.asm.service.QuizService;
import com.cognizant.asm.dto.request.CreateQuizRequest;
import com.cognizant.asm.dto.request.QuizAttemptRequest;
import com.cognizant.asm.dto.response.AssessmentSummaryResponse;
import com.cognizant.asm.dto.response.QuizAttemptResultResponse;
import com.cognizant.asm.dto.response.QuizDetailResponse;
import com.cognizant.asm.dto.response.ErrorResponseDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import jakarta.validation.Valid;

@Tag(
        name = "Quiz Management",
        description = "Handles operations related to quiz lifecycle. "
                + "Trainers can create and list quizzes, " +
                "while students (associates) can submit their answers and view calculated results."
)
@RestController
@RequestMapping("/assessments/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    private static final String QUIZ_DETAIL_EXAMPLE = """
        {
          "id": 1,
          "title": "Java Foundations",
          "type": "QUIZ",
          "status": "PUBLISHED",
          "batchId": 1001,
          "stageId": 1,
          "dueDate": "2026-05-15",
          "maxScore": 100,
          "durationMinutes": 30,
          "passingMarks": 50,
          "createdBy": 501,
          "questions": [
            {
              "id": 1,
              "questionText": "What is the primary purpose of the 'Optional' class in Java 8?",
              "optionA": "To increase execution speed.",
              "optionB": "To handle NullPointerExceptions more gracefully.",
              "optionC": "To create thread-safe variables.",
              "optionD": "To perform mathematical calculations.",
              "marks": 50
            },
            {
              "id": 2,
              "questionText": "Which of these is NOT a fundamental principle of Object-Oriented Programming?",
              "optionA": "Encapsulation",
              "optionB": "Inheritance",
              "optionC": "Polymorphism",
              "optionD": "Compilation",
              "marks": 50
            }
          ]
        }
        """;

    @Operation(
            summary = "Create a new quiz",
            description = "Allows trainers to create a quiz with multiple-choice questions. You must define questions, options, and the marks assigned to each correct answer."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Quiz created successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuizDetailResponse.class),
                            examples = @ExampleObject(value = QUIZ_DETAIL_EXAMPLE)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<QuizDetailResponse> createQuiz(@Valid @RequestBody CreateQuizRequest request, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long createdBy) {
        QuizDetailResponse response = quizService.createQuiz(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get quiz questions",
            description = "Fetches the full details of a specific quiz by its ID, including all associated questions and multiple-choice options."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quiz details retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuizDetailResponse.class),
                            examples = @ExampleObject(value = QUIZ_DETAIL_EXAMPLE)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No quiz found with the provided ID.")
    })
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDetailResponse> getQuizById(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizById(quizId));
    }

    @Operation(
            summary = "List all quizzes in a Batch",
            description = "Retrieves a summary of all quizzes assigned to a specific training batch."
    )
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(quizService.listQuizzesByBatch(batchId));
    }

    @Operation(
            summary = "Filter quizzes by Status in a Batch",
            description = "Retrieves quizzes for a specific batch, filtered by their current status (e.g., 'PUBLISHED' or 'DRAFT')."
    )
    @GetMapping("/batch/{batchId}/status/{status}")
    public ResponseEntity<List<AssessmentSummaryResponse>> listByBatchAndStatus(@PathVariable Long batchId, @PathVariable AssessmentStatus status) {
        return ResponseEntity.ok(quizService.listQuizzesByBatchAndStatus(batchId, status));
    }

    @Operation(
            summary = "Submit quiz attempt",
            description = "Allows students (associates) to submit their answers. The system immediately calculates the score and returns the final pass/fail result."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Quiz attempt submitted and processed successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuizAttemptResultResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "attemptId": 7,
                          "quizId": 1,
                          "quizTitle": "Java Foundations",
                          "associateId": 2486094,
                          "score": 100,
                          "maxScore": 100,
                          "passingMarks": 50,
                          "resultStatus": "PASS",
                          "submitted": true,
                          "submittedAt": "2026-05-18T16:00:00",
                          "answers": [
                            {
                              "id": 905,
                              "questionId": 1,
                              "selectedOption": "B",
                              "correct": true,
                              "marksAwarded": 50
                            },
                            {
                              "id": 906,
                              "questionId": 2,
                              "selectedOption": "D",
                              "correct": true,
                              "marksAwarded": 50
                            }
                          ]
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping("/{quizId}/attempt")
    public ResponseEntity<QuizAttemptResultResponse> attemptQuiz(@PathVariable Long quizId, @Valid @RequestBody QuizAttemptRequest request) {
        QuizAttemptResultResponse result = quizService.attemptQuiz(quizId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(
            summary = "Retrieve specific student result",
            description = "Fetches the score and submission status for a particular student's attempt on a specific quiz."
    )
    @GetMapping("/{quizId}/attempts/{associateId}/result")
    public ResponseEntity<QuizAttemptResultResponse> getAttemptResult(@PathVariable Long quizId, @PathVariable Long associateId) {
        return ResponseEntity.ok(quizService.getAttemptResult(quizId, associateId));
    }

    @Operation(
            summary = "List all attempts for a quiz",
            description = "Retrieves all student submission results for a specific quiz, which is useful for tracking overall batch progress."
    )
    @GetMapping("/{quizId}/attempts")
    public ResponseEntity<List<QuizAttemptResultResponse>> getAllAttempts(@PathVariable Long quizId) {;
        return ResponseEntity.ok(quizService.getAllAttempts(quizId));
    }
}