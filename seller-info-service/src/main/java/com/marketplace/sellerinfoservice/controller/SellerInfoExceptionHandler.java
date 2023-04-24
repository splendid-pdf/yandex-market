package com.marketplace.sellerinfoservice.controller;

import com.yandex.market.model.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class SellerInfoExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityExistExceptionHandler(EntityExistsException ex) {

        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Entity already exist in db. errorId = " + errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(EntityNotFoundException ex) {

        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Entity not found in db. errorId = " + errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }
}