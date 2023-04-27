package com.yandex.market.productservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record TypePreviewResponse(
        UUID id,
        String name
) {
}