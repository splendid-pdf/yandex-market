package com.yandex.market.userinfoservice.dto.request;

import java.util.UUID;

public record UserAuthenticationDto(
        UUID uuid,
        String email,
        String password,
        String role
) {
}