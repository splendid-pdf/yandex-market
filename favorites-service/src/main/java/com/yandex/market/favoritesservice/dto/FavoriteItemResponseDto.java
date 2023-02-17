package com.yandex.market.favoritesservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record FavoriteItemResponseDto(

        UUID productId,

        UUID userId,

        LocalDateTime additionTimestamp) {
}
