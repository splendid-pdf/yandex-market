package com.yandex.market.sellerinfoservice.controller;

import com.yandex.market.sellerinfoservice.controller.response.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class SellerInfoExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityExistExceptionHandler(EntityExistsException ex) {
        return ErrorResponse.builder()
                .statusCode(HttpStatus.CONFLICT.name())
                .message("User with email " + ex.getMessage() + " already exist")
                .timestamp(LocalDateTime.now())
                .build();
    }
}