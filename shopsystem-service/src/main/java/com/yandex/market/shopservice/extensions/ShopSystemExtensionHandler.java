package com.yandex.market.shopservice.extensions;

import com.yandex.market.shopservice.model.HistoryExceptions;
import com.yandex.market.shopservice.repositories.HistoryExceptionsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ShopSystemExtensionHandler {
    private final HistoryExceptionsRepository historyExceptionsRepository;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerEntityNotFoundException(EntityNotFoundException e) {
        historyExceptionsRepository.save(new HistoryExceptions("NOT_FOUND", e.getMessage()));
        return e.getMessage();
    }
}
