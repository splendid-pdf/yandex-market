package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FavoriteItemService {

    UUID addItemInFavorites(UUID productId, UUID userId);

    Page<FavoriteItemResponseDto> getFavoritesByUserId(UUID userId, Pageable page);

    void deleteFavoritesByUserId(UUID userId, UUID productId);
}
