package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.request.*;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import org.springframework.data.domain.*;

import java.util.UUID;

public interface FavoritesService {

    UUID addProductInFavorites(UUID userId, FavoriteProductRequest request);

    UUID addSellerInFavorites(UUID userId, FavoriteSellerRequest request);

    void deleteFavoriteProduct(UUID userId, UUID productId);

    void deleteFavoriteSeller(UUID userId, UUID sellerId);

    Page<FavoritePreview> getFavoriteProductsByUserId(UUID userId, Pageable page);

    Page<FavoritePreview> getFavoriteSellersByUserId(UUID userId, Pageable page);
}