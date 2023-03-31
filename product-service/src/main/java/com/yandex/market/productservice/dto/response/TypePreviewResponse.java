package com.yandex.market.productservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema
public record TypePreviewResponse(
        @NotBlank
        UUID id,
        String name
) {
}