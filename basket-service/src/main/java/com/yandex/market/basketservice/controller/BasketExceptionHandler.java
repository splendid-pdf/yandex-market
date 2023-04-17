package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.ErrorResponse;
import com.yandex.market.basketservice.errors.NotEnoughIGoodsInStockException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class BasketExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex){
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(NotEnoughIGoodsInStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotEnoughIGoodsInStockException(NotEnoughIGoodsInStockException ex){
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }
}
