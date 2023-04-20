package com.yandex.market.basketservice.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;


public record ErrorResponse(
        UUID id,

        String message,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime timestamp
) {
}
