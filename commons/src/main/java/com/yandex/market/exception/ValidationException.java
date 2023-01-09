package com.yandex.market.exception;

import lombok.Getter;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> exceptionMessages;

    public ValidationException(List<String> exceptionMessages){
        this.exceptionMessages = exceptionMessages;
    }

    public List<String> getExceptionMessages() {
        return exceptionMessages;
    }
}
