package com.marketplace.authorizationserver.dto;

import java.util.UUID;

public record UserAuthDto(
        UUID uuid,
        String email,
        String password,
        String role
) {
}