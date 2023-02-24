package com.yandex.market.userinfoservice.dto;

import java.util.UUID;

public record UserAuthenticationDto(
        UUID uuid,
        String email,
        String encodedPassword,
        String role
) {
}