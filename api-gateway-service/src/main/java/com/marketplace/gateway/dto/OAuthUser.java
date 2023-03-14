package com.marketplace.gateway.dto;

import java.time.Instant;

public record OAuthUser(
        String id,
        String email,
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiredAt
) {}