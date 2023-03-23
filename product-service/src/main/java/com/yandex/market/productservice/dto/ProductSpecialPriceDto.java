package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Schema
public record ProductSpecialPriceDto(
        @FutureOrPresent
        LocalDateTime fromDate,
        @FutureOrPresent
        LocalDateTime toDate,
        @PositiveOrZero
        Long price) {
}