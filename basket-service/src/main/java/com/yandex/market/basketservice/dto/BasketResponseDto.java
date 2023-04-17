package com.yandex.market.basketservice.dto;

import java.util.Set;
import java.util.UUID;

public record BasketResponseDto(
        UUID basket,
        UUID userId,
        Set<ProductResponseDto> productsSet
) {
}
