package com.yandex.market.productservice.exception;

import jakarta.validation.ValidationException;

public class InvalidCharacteristicsException extends ValidationException {

    public InvalidCharacteristicsException(String message) {
        super(message);
    }

}
