package com.yandex.market.basketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemRequest(

        @Schema(nullable = false, description = "Идентификатор продукта")
        @NotNull(message = "ID of item must be specified")
        UUID productId,

        @Schema(minimum = "1", description = "Количество добавляемого товара в корзину (шт.)")
        @NotNull(message = "The number of items must be specified")
        @Positive(message = "The number of items must be above zero")
        Integer numberOfItems
) {
}
