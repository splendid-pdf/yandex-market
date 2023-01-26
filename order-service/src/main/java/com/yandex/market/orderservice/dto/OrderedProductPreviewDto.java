package com.yandex.market.orderservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record OrderedProductPreviewDto(
        UUID productId,
        int amount,
        String name,
        String photoUrl) {
}