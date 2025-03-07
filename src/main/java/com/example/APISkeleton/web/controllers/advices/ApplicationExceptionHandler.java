package com.example.APISkeleton.web.controllers.advices;

import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        BaseResponse response = BaseResponse.builder()
                .data(errors)
                .message("Validation failed")
                .httpStatus(HttpStatus.BAD_GATEWAY)
                .success(false)
                .build();

        return response.buildResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException ex) {
        BaseResponse response = BaseResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(ex.getHttpStatus())
                .build();

        return response.buildResponseEntity();
    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<BaseResponse> handleConflictException(ConflictException ex) {
        BaseResponse response = BaseResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(ex.getHttpStatus())
                .build();

        return response.buildResponseEntity();
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<BaseResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        BaseResponse response = BaseResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(ex.getHttpStatus())
                .build();

        return response.buildResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        BaseResponse response = BaseResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(ex.getHttpStatus())
                .build();

        return response.buildResponseEntity();
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<BaseResponse> handleTokenExpiredException(TokenExpiredException ex) {
        BaseResponse response = BaseResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(ex.getHttpStatus())
                .build();

        return response.buildResponseEntity();
    }
}
