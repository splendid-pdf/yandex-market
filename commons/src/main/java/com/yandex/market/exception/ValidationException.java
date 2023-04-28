package com.yandex.market.exception;

import lombok.Getter;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> exceptionMessages;

    public ValidationException(List<String> exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }

    public ValidationException(String message) {
        super(message);
    }

    public List<String> getExceptionMessages() {
        return exceptionMessages;
    }
}
