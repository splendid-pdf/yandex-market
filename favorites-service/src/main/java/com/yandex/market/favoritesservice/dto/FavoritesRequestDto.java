package com.yandex.market.favoritesservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record FavoritesRequestDto(

        @NotNull(message = "\"Product\" field must not be null")
        UUID productId,

        LocalDateTime additionTimestamp) {
}
