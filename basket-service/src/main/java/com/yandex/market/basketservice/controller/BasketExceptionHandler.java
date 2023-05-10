package com.yandex.market.basketservice.controller;


import com.yandex.market.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class BasketExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled constraint violation error: msg = '{}', error_id = {}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled http message not readable error: msg = '{}', error_id = {}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled missing servlet request parameter error: msg = '{}', error_id = {}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handle entity not found error: msg = '{}', errorId = '{}'", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNullPointerException(NullPointerException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled null pointer error: msg = '{}', error_id = '{}'", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled method argument type mismatch error: msg = '{}', error_id = '{}'", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }
}
