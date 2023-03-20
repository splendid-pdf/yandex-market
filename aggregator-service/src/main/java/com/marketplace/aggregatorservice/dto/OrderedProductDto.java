package com.marketplace.aggregatorservice.dto;

import java.util.UUID;

public record OrderedProductDto(
        UUID productId,

        int amount,
        UUID branchId,
        UUID shopSystemId,
        String name,
        String description,
        double price,
        String photoUrl
) {
}