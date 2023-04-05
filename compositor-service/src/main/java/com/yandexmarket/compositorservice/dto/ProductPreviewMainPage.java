package com.yandexmarket.compositorservice.dto;

public record ProductPreviewMainPage(
        String id,
        String sellerId,
        String name,
        Long price,
        String imageUrl,
        String sellerCompanyName
) {
}