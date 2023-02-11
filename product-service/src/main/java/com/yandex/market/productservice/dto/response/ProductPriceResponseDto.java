package com.yandex.market.productservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductPriceResponseDto(
        UUID productId,
        UUID branchId,
        UUID shopSystemId,
        float price,
        float discountedPrice,
        LocalDateTime specialPriceFromDate,
        LocalDateTime specialPriceToDate) {
}
