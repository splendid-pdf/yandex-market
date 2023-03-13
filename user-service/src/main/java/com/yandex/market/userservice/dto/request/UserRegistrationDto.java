package com.yandex.market.userservice.dto.request;

public record UserRegistrationDto(
        String email,
        String password
) {
}