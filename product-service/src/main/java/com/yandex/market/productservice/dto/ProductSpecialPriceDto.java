package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record ProductSpecialPriceDto(
        @FutureOrPresent
        LocalDateTime specialPriceFromDate,
        @FutureOrPresent
        LocalDateTime specialPriceToDate,
        @PositiveOrZero
        Long specialPrice) {
}