package com.cognizant.tms.auth.manager.exception;

import com.cognizant.tms.auth.manager.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex,
                                                                        HttpServletRequest request){
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenInvalidException(TokenInvalidException ex,
                                                                        HttpServletRequest request){
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("T001");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoleNotFoundException(RoleNotFoundException ex,
                                                                        HttpServletRequest request){
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("R001");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, HttpServletRequest request){
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setTimestamp(LocalDateTime.now());
        response.setErrorCode("S500");
        response.setMessage("Error Occured");
        response.setPath(request.getRequestURI());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
