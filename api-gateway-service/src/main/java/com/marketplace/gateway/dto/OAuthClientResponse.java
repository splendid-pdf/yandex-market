package com.marketplace.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record OAuthClientResponse(String id, String email, String token) {}