package com.yandex.market.productservice.models;

import java.util.UUID;

public record ArchiveResponse(
        UUID id,
        String name,
        UUID sellerId,
        String brand,
        Long price,
        Long count,
        String type,
        String creationDate,
        String imageUrl) {
}