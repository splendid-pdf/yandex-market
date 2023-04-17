package com.yandex.market.basketservice.dto;

import java.util.UUID;

public record ProductResponseDto(
        UUID productId,
        Integer totalNumberItemsInBasket
) {
}
