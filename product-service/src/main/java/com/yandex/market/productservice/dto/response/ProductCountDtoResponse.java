package com.yandex.market.productservice.dto.response;

public record ProductCountDtoResponse(
        String productId,
        Long count,
        String status,
        String reason
) {
}
