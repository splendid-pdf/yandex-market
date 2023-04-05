package com.yandexmarket.compositorservice.dto;

public record ProductPreview(
        String id,
        String sellerId,
        String name,
        Long price,
        String imageUrl
) {
}
