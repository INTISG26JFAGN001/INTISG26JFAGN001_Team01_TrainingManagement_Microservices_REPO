package com.cognizant.tes.exception;

import com.cognizant.tes.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    // Associate already exists → 409 Conflict
//    @ExceptionHandler(InvalidAssociateException.class)
//    public ResponseEntity<ErrorResponseDTO> handleInvalidAssociate(InvalidAssociateException ex, HttpServletRequest request) {
//        return buildErrorResponse(HttpStatus.CONFLICT, "TES-409", ex.getMessage(), request.getRequestURI());
//    }
//
//    // Batch not found → 404
//    @ExceptionHandler(InvalidBatchException.class)
//    public ResponseEntity<ErrorResponseDTO> handleInvalidBatch(InvalidBatchException ex, HttpServletRequest request) {
//        return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
//    }
@ExceptionHandler(InvalidAssociateException.class)
public ResponseEntity<ErrorResponseDTO> handleInvalidAssociate(InvalidAssociateException ex, HttpServletRequest request) {
    // Decide based on message or context
    if (ex.getMessage().contains("exists")) {
        return buildErrorResponse(HttpStatus.CONFLICT, "TES-409", ex.getMessage(), request.getRequestURI());
    }
    return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
}

    // Enrollment invalid (e.g. bad input) → 400
    @ExceptionHandler(InvalidEnrollmentException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidEnrollment(InvalidEnrollmentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
    }

    // Schedule invalid → 400
    @ExceptionHandler(InvalidScheduleException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidSchedule(InvalidScheduleException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
    }

    // Trainer not found → 404
    @ExceptionHandler(InvalidTrainerException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTrainer(InvalidTrainerException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidBatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTrainer(InvalidBatchException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "TES-404", ex.getMessage(), request.getRequestURI());
    }

    // Validation errors from @Valid → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "Validation failed";
        }
        logger.warn("Validation error at {}: {}", request.getRequestURI(), message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "TES-400", message, request.getRequestURI());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidArgument(
            InvalidArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "TES-400", ex.getMessage(), request.getRequestURI());
    }

    // Database constraint violations (unique fields) → 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        logger.warn("Data integrity violation at {}: {}", request.getRequestURI(), ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "TES-409", "Duplicate value violates unique constraint", request.getRequestURI());
    }

    // Catch-all → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "TES-500", "Unexpected server error", request.getRequestURI());
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String errorCode, String message, String path) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode(errorCode);
        response.setMessage(message);
        response.setPath(path);
        return ResponseEntity.status(status).body(response);
    }

    private String formatFieldError(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage() == null ? "invalid value" : fieldError.getDefaultMessage();
        return fieldError.getField() + ": " + defaultMessage;
    }
}
