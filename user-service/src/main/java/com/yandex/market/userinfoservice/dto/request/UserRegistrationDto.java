package com.yandex.market.userinfoservice.dto.request;

public record UserRegistrationDto(
        String email,
        String password
) {
}