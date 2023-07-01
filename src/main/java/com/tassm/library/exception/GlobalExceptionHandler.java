package com.tassm.library.exception;

import com.tassm.library.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(
            ResourceNotFoundException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDTO> handleException(
            ResourceConflictException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.CONFLICT.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleException(
            BadRequestException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleException(
            MethodArgumentNotValidException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(
            Exception exception, ServletWebRequest request) {
        ErrorDTO error =
                new ErrorDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unhandled exception occurred: " + exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
