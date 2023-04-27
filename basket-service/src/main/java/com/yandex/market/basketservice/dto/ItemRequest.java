package com.yandex.market.basketservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemRequest(

        @NotNull(message = "ID of item must be specified")
        UUID productId,
        
        @NotNull(message = "The number of items must be specified")
        @Positive(message = "The number of items must be above zero")
        Integer numberOfItems
) {
}
