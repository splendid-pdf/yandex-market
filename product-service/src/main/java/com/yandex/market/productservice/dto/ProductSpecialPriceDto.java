package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record ProductSpecialPriceDto(
        @FutureOrPresent
        LocalDateTime specialPriceFromDate,
        @FutureOrPresent
        LocalDateTime specialPriceToDate,
        @PositiveOrZero
        Long specialPrice) {
}