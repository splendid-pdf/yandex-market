package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.service.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ShopSystemControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("EntityNotFoundException: entity does not found in datasource")
                .debugMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ExceptionResponse handleSQLException(SQLException ex) {
        if ("23505".equals(ex.getSQLState())) {
            return ExceptionResponse.builder()
                    .timeStamped(LocalDateTime.now())
                    .message("SQLException: The uniqueness of the key has been violated")
                    .debugMessage(ex.getMessage())
                    .errorCode(ex.getErrorCode())
                    .build();
        } else {
            return ExceptionResponse.builder()
                    .timeStamped(LocalDateTime.now())
                    .message("SQLException")
                    .debugMessage(ex.getMessage())
                    .errorCode(ex.getErrorCode())
                    .build();
        }
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ExceptionResponse handleRuntimeException(RuntimeException ex) {
        return ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("RuntimeException")
                .debugMessage(ex.getMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("HttpRequestMethodNotSupported: Malformed JSON Request")
                .debugMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("MethodArgumentNotValid: Method Argument Not Valid")
                .debugMessage(ex.getMessage())
                .errors(errors)
                .build();
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
