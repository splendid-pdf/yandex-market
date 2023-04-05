package com.yandex.market.model;

import java.time.OffsetDateTime;
import java.util.UUID;


public record ErrorResponse(String id, String message, OffsetDateTime timestamp) {
}
