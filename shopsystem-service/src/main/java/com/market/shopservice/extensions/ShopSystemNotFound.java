package com.market.shopservice.extensions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopSystemNotFound extends RuntimeException {
    public ShopSystemNotFound(String message) {
        super(message);
    }
}
