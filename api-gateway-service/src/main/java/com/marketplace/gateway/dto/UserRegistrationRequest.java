package com.marketplace.gateway.dto;

public record UserRegistrationRequest(
        String email,
        String password
) {
}