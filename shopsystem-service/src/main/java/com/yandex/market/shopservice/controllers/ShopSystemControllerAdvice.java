package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.service.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ShopSystemControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException ex) {
        ExceptionResponse exceptionResponse;
        if ("23505".equals(ex.getSQLState())) {
            exceptionResponse = ExceptionResponse.builder()
                    .timeStamped(LocalDateTime.now())
                    .message("The uniqueness of the key has been violated")
                    .debugMessage(ex.getMessage())
                    .errorCode(ex.getErrorCode())
                    .SQLState(ex.getSQLState())
                    .build();
        } else {
            exceptionResponse = ExceptionResponse.builder()
                    .timeStamped(LocalDateTime.now())
                    .message("SQLException")
                    .debugMessage(ex.getMessage())
                    .errorCode(ex.getErrorCode())
                    .SQLState(ex.getSQLState())
                    .build();
        }
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("Malformed JSON Request")
                .debugMessage(ex.getMessage())
                .statusCode(ex.getStatusCode())
                .build();
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("Method Argument Not Valid")
                .debugMessage(ex.getMessage())
                .statusCode(ex.getStatusCode())
                .errors(errors)
                .build();
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
