package com.marketplace.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record LoginRequest(String email, String password) {}