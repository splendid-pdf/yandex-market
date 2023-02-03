package com.yandex.market.orderservice.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("EntityNotFoundException: entity does not found in datasource")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedOperationException.class)
    public ExceptionResponse handleEntityNotFoundException(UnsupportedOperationException ex) {
        return ExceptionResponse.builder()
                .timeStamped(LocalDateTime.now())
                .message("EntityNotFoundException: entity does not found in datasource")
                .build();
    }
}