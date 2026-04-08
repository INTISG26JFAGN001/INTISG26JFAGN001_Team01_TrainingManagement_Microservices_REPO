package com.cognizant.cat.exception;

import com.cognizant.cat.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                             HttpServletRequest request){
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("A001");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("A002");
        response.setMessage(errorMessage);
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest request) {

        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("A003");
        response.setMessage("Something went wrong");
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
