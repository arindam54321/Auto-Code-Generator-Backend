package com.ari.auto.code.generator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> data = e.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ApiResult.createFailureResult(data, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper<List<String>>> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> data = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ApiResult.createFailureResult(data, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<String>> handleException(Exception e) {
        String message = (e.getMessage() == null || e.getMessage().isEmpty())
                ? "Unknown Exception! Please contact the developers!"
                : e.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ApiResult.createFailureResult(message, status);
    }
}
