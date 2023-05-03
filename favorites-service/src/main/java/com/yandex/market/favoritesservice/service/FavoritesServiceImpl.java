package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.mapper.FavoriteProductMapper;
import com.yandex.market.favoritesservice.mapper.FavoriteSellerMapper;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import com.yandex.market.favoritesservice.model.FavoriteSeller;
import com.yandex.market.favoritesservice.repository.FavoriteProductRepository;
import com.yandex.market.favoritesservice.repository.FavoriteSellerRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoriteSellerMapper favoriteSellerMapper;

    private final FavoriteProductMapper favoriteProductMapper;

    private final FavoriteSellerRepository favoriteSellerRepository;

    private final FavoriteProductRepository favoriteProductRepository;

    @Transactional
    public UUID addProductInFavorites(UUID userId, FavoriteProductRequest request) {
        FavoriteProduct product = favoriteProductMapper.toFavoriteProduct(request);
        product.setUserId(userId);

        if (favoriteProductRepository.existsFavoriteProductByExternalIdAndUserId(product.getExternalId(), userId)) {
            throw new EntityExistsException("This user already has this product");
        } else {
            favoriteProductRepository.save(product);
            return product.getExternalId();
        }
    }

    @Transactional
    public UUID addSellerInFavorites(UUID userId, FavoriteSellerRequest request) {
        FavoriteSeller seller = favoriteSellerMapper.toFavoriteSeller(request);
        seller.setUserId(userId);

        if (favoriteSellerRepository.existsFavoriteSellerByExternalIdAndUserId(seller.getExternalId(), userId)) {
            throw new EntityExistsException("This user already has this seller");
        } else {
            favoriteSellerRepository.save(seller);
            return seller.getExternalId();
        }
    }

    @Transactional
    public void deleteFavoriteProduct(UUID userId, UUID productId) {
        favoriteProductRepository.deleteByExternalIdAndUserId(productId, userId);
    }

    @Transactional
    public void deleteFavoriteSeller(UUID userId, UUID sellerId) {
        favoriteSellerRepository.deleteByExternalIdAndUserId(sellerId, userId);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteProductsByUserId(UUID userId, Pageable page) {
        return favoriteProductRepository.findFavoriteProductsByUserId(userId, page);
    }

    @Transactional(readOnly = true)
    public Page<FavoritePreview> getFavoriteSellersByUserId(UUID userId, Pageable page) {
        return favoriteSellerRepository.findFavoriteSellersByUserId(userId, page);
    }
}