package com.yandex.market.favoritesservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema
public record FavoriteSellerRequest(

        @NotNull(message = "The ID of the product must be indicated")
        String sellerId
) {
}