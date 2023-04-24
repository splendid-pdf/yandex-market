package com.marketplace.userservice.dto.request;

public record UserRegistrationDto(
        String email,
        String password
) {
}