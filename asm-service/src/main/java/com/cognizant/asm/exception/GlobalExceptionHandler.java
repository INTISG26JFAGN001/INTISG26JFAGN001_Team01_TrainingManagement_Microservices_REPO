package com.cognizant.asm.exception;

import com.cognizant.asm.dto.response.ErrorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - NOT FOUND

    @ExceptionHandler(AssessmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAssessmentNotFound(AssessmentNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(AttemptNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAttemptNotFound(AttemptNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RubricNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRubricNotFound(RubricNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // 409 - CONFLICT

    @ExceptionHandler(DuplicateAttemptException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateAttempt(DuplicateAttemptException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    // 400 - BAD REQUEST

    @ExceptionHandler(AssessmentNotAvailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotAvailable(AssessmentNotAvailableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RubricWeightExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleWeightExceeded(RubricWeightExceededException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RubricTotalNotHundredException.class)
    public ResponseEntity<ErrorResponseDTO> handleRubricTotalNotHundred(RubricTotalNotHundredException ex, HttpServletRequest request) {
        // 422 Unprocessable Entity — rubric weights don't total 100
        return buildResponse422(ex.getMessage(), request);
    }

    // Handles Bean Validation errors (@Valid on request bodies)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "One or more fields failed validation. See 'fieldErrors' for details.", request, fieldErrors);
    }

    // 500 - Internal Server Error (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAll(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request, null);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message, HttpServletRequest request, Map<String, String> fieldErrors) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse422(String message, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                422,
                "Unprocessable Entity",
                message,
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(response);
    }
}