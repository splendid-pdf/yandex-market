package com.yandex.market.shopservice.extensions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShopSystemExtensionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerShopSystemNotFound(ShopSystemNotFound e) {
        return e.getMessage();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerShopSystemDtoIsEmpty(ShopSystemDtoIsEmpty e) {
        return e.getMessage();
    }
}
