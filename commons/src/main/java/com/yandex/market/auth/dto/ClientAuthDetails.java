package com.yandex.market.auth.dto;

import java.util.UUID;

public record ClientAuthDetails(
        UUID uuid,
        String email,
        String password,
        String role
) {
}