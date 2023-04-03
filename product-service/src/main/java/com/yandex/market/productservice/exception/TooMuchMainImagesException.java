package com.yandex.market.productservice.exception;

import jakarta.validation.ValidationException;

public class TooMuchMainImagesException extends ValidationException {


    public TooMuchMainImagesException(String message) {
        super(message);
    }
}
