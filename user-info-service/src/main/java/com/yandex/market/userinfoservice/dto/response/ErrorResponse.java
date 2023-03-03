package com.yandex.market.userinfoservice.dto.response;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record ErrorResponse(
        String statusCode,
        String message,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime timestamp
) {
}