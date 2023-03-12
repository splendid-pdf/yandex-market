package com.marketplace.authorizationserver.dto;

public record OAuthUserData(String id, String email, String accessToken, String refreshToken) {}