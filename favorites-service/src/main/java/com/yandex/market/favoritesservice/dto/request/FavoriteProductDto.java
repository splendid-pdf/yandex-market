package com.yandex.market.favoritesservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FavoriteProductDto(

        @NotNull(message = "The ID of the product must be indicated")
        UUID productId
) {}