package com.tassm.library.exception;

import com.tassm.library.model.dto.ErrorDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle resource not found as 404
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(
            ResourceNotFoundException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle resource conflict as HTTP 409
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDTO> handleException(
            ResourceConflictException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.CONFLICT.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handle validation error as as HTTP 400
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleException(
            BadRequestException exception, ServletWebRequest request) {
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation error as as HTTP 400
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleException(
            MethodArgumentNotValidException exception, ServletWebRequest request) {
        ErrorDTO error =
                new ErrorDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request body or parameter - try again");
        exception.printStackTrace();
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint violation error as as HTTP 400
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleException(
            ConstraintViolationException exception, ServletWebRequest request) {
        ErrorDTO error =
                new ErrorDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request body or parameter - try again");
        exception.printStackTrace();
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle any other exception as HTTP 500
     *
     * @param exception
     * @param request
     * @return ResponseEntity<ErrorDTO> The response generated describing the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(
            Exception exception, ServletWebRequest request) {
        ErrorDTO error =
                new ErrorDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unhandled exception occurred, see appliction logs");
        exception.printStackTrace();
        return new ResponseEntity<ErrorDTO>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
