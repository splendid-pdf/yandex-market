package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.response.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleEntityNotFoundEx(EntityNotFoundException ex) {
        return new ExceptionResponse("Entity not found exception", ex.getMessage());
    }

}
