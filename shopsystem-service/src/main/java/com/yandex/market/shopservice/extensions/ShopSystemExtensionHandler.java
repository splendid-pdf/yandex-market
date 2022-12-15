package com.yandex.market.shopservice.extensions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class ShopSystemExtensionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerEntityNotFoundException(EntityNotFoundException e) {
        return "EntityNotFoundException. " + e.getMessage() + ". " + LocalDateTime.now();
    }
}
