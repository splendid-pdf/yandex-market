package com.yandex.market.orderservice.dto.seller;

public record SellerOrderProductPreview(
        String photoUrl,
        String name,
        String articleFromSeller,
        int amount
) {
}