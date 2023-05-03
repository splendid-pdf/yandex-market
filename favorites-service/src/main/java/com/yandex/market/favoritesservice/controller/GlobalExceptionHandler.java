package com.yandex.market.favoritesservice.controller;

import com.yandex.market.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                ex.getMessage(),
                OffsetDateTime.now()
        );

        logging(errorResponse.message(), errorResponse.id());
        return errorResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                ex.getMessage(),
                OffsetDateTime.now()
        );

        logging(errorResponse.message(), errorResponse.id());
        return errorResponse;
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                ex.getMessage(),
                OffsetDateTime.now()
        );

        logging(errorResponse.message(), errorResponse.id());
        return new ResponseEntity<>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                ex.getMessage(),
                OffsetDateTime.now()
        );

        logging(errorResponse.message(), errorResponse.id());
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logging(String debugMsg, String id) {
        if (log.isDebugEnabled()) {
            log.error("Handled http message not readable error: msg = '{}', exception_id = {}", debugMsg, id);
        }
    }
}