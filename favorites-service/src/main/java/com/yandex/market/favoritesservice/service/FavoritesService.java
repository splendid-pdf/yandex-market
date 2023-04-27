package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.response.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.dto.request.FavoriteProductDto;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FavoritesService {

    UUID addProductInFavorites(UUID userId, FavoriteProductDto favoriteProductDto);

    void deleteFavoriteProductByUserIdAndProductId(UUID userId, UUID productId);

    Page<FavoriteItemResponseDto> getFavoritesByUserId(UUID userId, Pageable page);
}