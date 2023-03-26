package com.marketplace.gateway.dto;

import java.time.OffsetDateTime;

public record ServerErrorResponse(String path, String message, String statusCode, OffsetDateTime timestamp) {
}