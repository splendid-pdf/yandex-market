package com.yandex.market.favoritesservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema
public record FavoriteProductRequest(

        @NotNull(message = "The ID of the product must be indicated")
        UUID productId
) {
}