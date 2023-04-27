package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.mapper.FavoriteProductMapper;
import com.yandex.market.favoritesservice.mapper.FavoriteSellerMapper;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import com.yandex.market.favoritesservice.model.FavoriteSeller;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import com.yandex.market.favoritesservice.repository.FavoriteProductRepository;
import com.yandex.market.favoritesservice.repository.FavoriteSellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoriteSellerMapper favoriteSellerMapper;

    private final FavoriteProductMapper favoriteProductMapper;

    private final FavoriteItemRepository favoriteItemRepository;

    private final FavoriteSellerRepository favoriteSellerRepository;

    private final FavoriteProductRepository favoriteProductRepository;

    @Transactional
    public UUID addProductInFavorites(UUID userId, FavoriteProductRequest request) {
        FavoriteItem favoriteItem = getOrCreateFavoriteItem(userId);
        FavoriteProduct favoriteProduct = favoriteProductMapper.toFavoriteProduct(request);

        if (favoriteProductRepository.findFavoriteItemByFavoriteItemAndExternalId(
                favoriteItem,
                favoriteProduct.getExternalId()).isEmpty()) {

            favoriteItem.addProduct(favoriteProduct);
            favoriteItemRepository.save(favoriteItem);
        }
        return favoriteProduct.getExternalId();
    }

    @Transactional
    public UUID addSellerInFavorites(UUID userId, FavoriteSellerRequest request) {
        FavoriteItem favoriteItem = getOrCreateFavoriteItem(userId);
        FavoriteSeller favoriteSeller = favoriteSellerMapper.toFavoriteSeller(request);

        if (favoriteSellerRepository.findFavoriteItemByFavoriteItemAndExternalId(
                favoriteItem,
                favoriteSeller.getExternalId()).isEmpty()) {

            favoriteItem.addSeller(favoriteSeller);
            favoriteItemRepository.save(favoriteItem);
        }
        return favoriteSeller.getExternalId();
    }

    @Transactional
    public void deleteFavoriteProduct(UUID userId, UUID productId) {
        FavoriteItem favoriteItem = findFavoriteItem(userId);
        favoriteProductRepository.deleteByFavoriteItemAndExternalId(favoriteItem, productId);
    }

    @Transactional
    public void deleteFavoriteSeller(UUID userId, UUID sellerId) {
        FavoriteItem favoriteItem = findFavoriteItem(userId);
        favoriteSellerRepository.deleteByFavoriteItemAndExternalId(favoriteItem, sellerId);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteProductsByUserId(UUID userId, Pageable page) {
        return favoriteProductRepository.findFavoriteProductsByUserId(userId, page);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteSellersByUserId(UUID userId, Pageable page) {
        return favoriteSellerRepository.findFavoriteSellersByUserId(userId, page);
    }

    private FavoriteItem findFavoriteItem(UUID userId) {
        return favoriteItemRepository.findFavoriteItemByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Favorite item was not found by given user ID = %s".formatted(userId))
        );
    }

    private FavoriteItem getOrCreateFavoriteItem(UUID userId) {
        Optional<FavoriteItem> favoriteItem = favoriteItemRepository.findFavoriteItemByUserId(userId);

        return favoriteItem.orElseGet(
                () -> favoriteItemRepository.save(
                        FavoriteItem.builder()
                                .userId(userId)
                                .build()));
    }
}