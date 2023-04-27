package com.yandex.market.favoritesservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record FavoriteSellerDto(

        @NotNull(message = "The ID of the product must be indicated")
        String sellerId
) {}