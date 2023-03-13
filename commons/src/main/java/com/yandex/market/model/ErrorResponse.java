package com.yandex.market.model;

import java.time.OffsetDateTime;
import java.util.UUID;


public record ErrorResponse(String message, UUID errorId, OffsetDateTime timestamp) {
}
