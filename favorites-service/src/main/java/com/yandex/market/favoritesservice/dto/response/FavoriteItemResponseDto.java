package com.yandex.market.favoritesservice.dto.response;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductDto;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Schema
public record FavoriteItemResponseDto(

        UUID externalId,

        UUID userId,

        Page<FavoriteProductDto> products,

        Page<FavoriteSellerDto> sellers
) {}