package com.marketplace.gateway.dto;

public record OAuthUserData(String id, String email, String accessToken, String refreshToken) {}