package com.marketplace.userservice.controller;

import com.yandex.market.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class UserInfoExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeValidationException(ValidationException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled validation error: msg='{}', errorId='{}", ex.getMessage(), errorId);
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
            log.error("Handled entity not found error: msg='{}', errorId='{}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(DateTimeParseException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled date-time parse error: msg='{}', errorId='{}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                "Invalid data format. Stick to format: yyyy-MM-dd",
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Handled illegal argument error: msg='{}', errorId='{}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }
}