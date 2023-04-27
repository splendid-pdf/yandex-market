package com.yandex.market.productservice.models;

import java.util.UUID;

public record ProductsResponse(
        String name,
        UUID id,
        String brand,
        String description,
        boolean isVisible,
        UUID sellerId,
        Long price,
        Long count,
        String type,
        String creationDate) {
}
