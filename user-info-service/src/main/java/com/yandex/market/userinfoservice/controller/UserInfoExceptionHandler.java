package com.yandex.market.userinfoservice.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.openapitools.api.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class UserInfoExceptionHandler {

    //todo: put to another package

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeValidationException(ValidationException ex) {
        return new ErrorResponse()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorResponse()
                .statusCode(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(DateTimeParseException ex) {
        return new ErrorResponse()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .message("Invalid data format. Stick to format: yyyy-MM-dd")
                .timestamp(OffsetDateTime.now());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now());

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(HttpMessageNotReadableException ex) {
        return new ErrorResponse()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .message("JSON parse error! Your JSON has the wrong form")
                .timestamp(OffsetDateTime.now());

    }
}