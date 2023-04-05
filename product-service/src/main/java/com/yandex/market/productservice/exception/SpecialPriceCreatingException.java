package com.yandex.market.productservice.exception;

import jakarta.validation.ValidationException;

public class SpecialPriceCreatingException extends ValidationException {

    public SpecialPriceCreatingException(String message) {
        super(message);
    }
}
