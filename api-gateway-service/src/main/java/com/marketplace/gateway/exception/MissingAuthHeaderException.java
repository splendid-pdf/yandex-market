package com.marketplace.gateway.exception;

public class MissingAuthHeaderException extends RuntimeException {
    public static final int STATUS_CODE = 401;

    public MissingAuthHeaderException(String message) {
        super(message);
    }
}