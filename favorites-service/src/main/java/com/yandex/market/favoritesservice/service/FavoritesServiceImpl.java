package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.mapper.FavoriteProductMapper;
import com.yandex.market.favoritesservice.mapper.FavoriteSellerMapper;
import com.yandex.market.favoritesservice.model.UserFavoritesPage;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import com.yandex.market.favoritesservice.model.FavoriteSeller;
import com.yandex.market.favoritesservice.repository.UserFavoritesPageRepository;
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

    private final UserFavoritesPageRepository userFavoritesPageRepository;

    private final FavoriteSellerRepository favoriteSellerRepository;

    private final FavoriteProductRepository favoriteProductRepository;

    @Transactional
    public UUID addProductInFavorites(UUID userId, FavoriteProductRequest request) {
        UserFavoritesPage userFavoritesPage = getOrCreateUserFavoritesPage(userId);
        FavoriteProduct favoriteProduct = favoriteProductMapper.toFavoriteProduct(request);

        if (favoriteProductRepository.findFavoriteItemByUserFavoritesPageAndExternalId(
                userFavoritesPage,
                favoriteProduct.getExternalId()).isEmpty()) {

            userFavoritesPage.addProduct(favoriteProduct);
            userFavoritesPageRepository.save(userFavoritesPage);
        }
        return favoriteProduct.getExternalId();
    }

    @Transactional
    public UUID addSellerInFavorites(UUID userId, FavoriteSellerRequest request) {
        UserFavoritesPage userFavoritesPage = getOrCreateUserFavoritesPage(userId);
        FavoriteSeller favoriteSeller = favoriteSellerMapper.toFavoriteSeller(request);

        if (favoriteSellerRepository.findByUserFavoritesPageAndExternalId(
                userFavoritesPage,
                favoriteSeller.getExternalId()).isEmpty()) {

            userFavoritesPage.addSeller(favoriteSeller);
            userFavoritesPageRepository.save(userFavoritesPage);
        }
        return favoriteSeller.getExternalId();
    }

    @Transactional
    public void deleteFavoriteProduct(UUID userId, UUID productId) {
        UserFavoritesPage userFavoritesPage = findUserFavoritesPage(userId);
        favoriteProductRepository.deleteByUserFavoritesPageAndExternalId(userFavoritesPage, productId);
    }

    @Transactional
    public void deleteFavoriteSeller(UUID userId, UUID sellerId) {
        UserFavoritesPage userFavoritesPage = findUserFavoritesPage(userId);
        favoriteSellerRepository.deleteByUserFavoritesPageAndExternalId(userFavoritesPage, sellerId);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteProductsByUserId(UUID userId, Pageable page) {
        return favoriteProductRepository.findFavoriteProductsByUserId(userId, page);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteSellersByUserId(UUID userId, Pageable page) {
        return favoriteSellerRepository.findFavoriteSellersByUserId(userId, page);
    }

    private UserFavoritesPage findUserFavoritesPage(UUID userId) {
        return userFavoritesPageRepository.findUserFavoritesPageByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Favorite item was not found by given user ID = %s".formatted(userId))
        );
    }

    private UserFavoritesPage getOrCreateUserFavoritesPage(UUID userId) {
        Optional<UserFavoritesPage> userFavoritesPage = userFavoritesPageRepository.findUserFavoritesPageByUserId(userId);

        return userFavoritesPage.orElseGet(
                () -> userFavoritesPageRepository.save(
                        UserFavoritesPage.builder()
                                .userId(userId)
                                .build()));
    }
}