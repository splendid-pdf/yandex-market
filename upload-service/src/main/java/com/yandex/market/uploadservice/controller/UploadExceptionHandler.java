package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.exception.NotFoundException;
import com.yandex.market.exception.SizeLimitFileExceededException;
import com.yandex.market.exception.ValidationException;
import com.yandex.market.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class UploadExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
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