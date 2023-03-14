package com.marketplace.gateway.exception;

public class AuthenticationException extends RuntimeException {
    public static final int STATUS_CODE = 401;

    public AuthenticationException(String message) {
        super(message);
    }
}