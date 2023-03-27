package com.yandex.market.productservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Schema
public record ProductSpecialPriceRequest(
        @FutureOrPresent
        LocalDateTime fromDate,
        @FutureOrPresent
        LocalDateTime toDate,
        @PositiveOrZero
        Long price
) {
}
