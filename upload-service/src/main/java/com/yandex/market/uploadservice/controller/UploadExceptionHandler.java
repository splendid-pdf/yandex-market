package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.exception.NotFoundException;
import com.yandex.market.exception.SizeLimitFileExceededException;
import com.yandex.market.exception.ValidationException;
import com.yandex.market.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class UploadExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                e.getMessage(),
                OffsetDateTime.now()
        );
        log.debug("Error processed with incorrect data entered");
        return errorResponse;
    }

    @ExceptionHandler(AmazonS3Exception.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleAmazonS3Exception(AmazonS3Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                e.getErrorMessage(),
                OffsetDateTime.now()
        );
        log.debug("");
        return errorResponse;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                e.getMessage(),
                OffsetDateTime.now()
        );
        log.debug("Error in incorrectly transmitted data");
        return errorResponse;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                e.getMessage(),
                OffsetDateTime.now()
        );
        log.debug("Can not found entity from UUID list");
        return errorResponse;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                UUID.randomUUID().toString(),
                e.getMessage(),
                OffsetDateTime.now()
        );
        log.debug("Validation exception");
        return errorResponse;
    }

}