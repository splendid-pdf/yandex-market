package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FavoritesService {

    UUID createFavorites(FavoritesRequestDto favoritesRequestDto, UUID userId);

    Page<FavoritesResponseDto> getFavoritesByUserId(UUID userId, Pageable page);

    void deleteFavoritesByUserId(UUID userId, UUID productId);
}
