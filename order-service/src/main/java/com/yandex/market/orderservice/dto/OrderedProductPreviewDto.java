package com.yandex.market.orderservice.dto;


import java.util.UUID;

public record OrderedProductPreviewDto(
        UUID productId,
        int amount,
        String name,
        String photoUrl) {
}