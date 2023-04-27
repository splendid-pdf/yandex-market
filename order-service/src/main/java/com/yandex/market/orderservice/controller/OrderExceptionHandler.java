package com.yandex.market.orderservice.controller;

import com.yandex.market.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class OrderExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Entity exist exception: massage={}, errorId={}", ex.getMessage(), errorId);
        }

        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedOperationException.class)
    public ErrorResponse handleEntityNotFoundException(UnsupportedOperationException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Unsupported operation exception: massage={}, errorId={}", ex.getMessage(), errorId);
        }
        return new ErrorResponse(
                errorId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }
}