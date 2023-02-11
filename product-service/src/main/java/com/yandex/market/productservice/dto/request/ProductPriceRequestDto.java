package com.yandex.market.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductPriceRequestDto(
        @NotBlank(message = "\"Product Id\" field must not be empty")
        UUID productId,

        @NotBlank(message = "\"Branch Id\" field must not be empty")
        UUID branchId,

        @NotBlank(message = "\"Shop System Id\" field must not be empty")
        UUID shopSystemId,

        @NotBlank(message = "\"Price\" field must not be empty and negative")
        @Positive
        float price,

        @NotBlank(message = "\"Discounted Price\" field must not be empty and negative")
        @Positive
        float discountedPrice,

        LocalDateTime specialPriceFromDate,

        LocalDateTime specialPriceToDate) {
}
