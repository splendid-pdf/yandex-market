package com.yandex.market.productservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Schema
public record SpecialPriceRequest(

        @NotNull(message = "The start date of the promotion must be specified")
        @FutureOrPresent(message = "The promotion cannot start in the past")
        LocalDateTime fromDate,

        @NotNull(message = "The end date of the promotion must be specified")
        @FutureOrPresent(message = "The promotion cannot be already completed")
        LocalDateTime toDate,

        @NotNull(message = "The discounted price must be indicated")
        @PositiveOrZero(message = "The price must be greater than zero")
        Long price
) {
}
