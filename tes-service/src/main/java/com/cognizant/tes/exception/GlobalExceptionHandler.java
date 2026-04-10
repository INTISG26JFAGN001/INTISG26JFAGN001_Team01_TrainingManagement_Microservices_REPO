package com.cognizant.tes.exception;

import com.cognizant.tes.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler({
            InvalidAssociateException.class,
            InvalidBatchException.class,
            InvalidEnrollmentException.class,
            InvalidScheduleException.class,
            InvalidTrainerException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        logger.error("Domain exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "TES-404",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        }

        logger.error("Validation error at {}: {}", request.getRequestURI(), message);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "TES-400",
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "TES-500",
                "Unexpected server error",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status,
                                                                String errorCode,
                                                                String message,
                                                                String path) {
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
