package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductDto;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerDto;
import com.yandex.market.favoritesservice.dto.response.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.mapper.FavoriteItemMapper;
import com.yandex.market.favoritesservice.mapper.FavoriteProductMapper;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoriteItemMapper favoriteItemMapper;

    private final FavoriteProductMapper favoriteProductMapper;

    private final FavoriteItemRepository favoriteItemRepository;

    @Override
    public UUID addProductInFavorites(UUID userId, FavoriteProductDto favoriteProductDto) {
        Optional<FavoriteItem> favoriteItem = favoriteItemRepository.getFavoriteItemByUserId(userId);
        FavoriteProduct favoriteProduct = favoriteProductMapper.toFavoriteProduct(favoriteProductDto);

        if (favoriteItem.isPresent()) {
            favoriteItemRepository.save(favoriteItem.get().addProduct(favoriteProduct));
        } else {
            favoriteItemRepository.save(
                    new FavoriteItem()
                            .setUserId(userId)
                            .setExternalId(UUID.randomUUID())
                            .addProduct(favoriteProduct));
        }
        return favoriteProduct.getExternalId();
    }

    @Override
    @Transactional
    public Page<FavoriteItemResponseDto> getFavoritesByUserId(UUID userId, Pageable page) {
        Page<FavoriteItem> pageFavorites = favoriteItemRepository.getFavoritesByUserId(userId, page);
        return new PageImpl<>(pageFavorites.getContent().stream()
                .map(favoriteItemMapper::toFavoritesResponseDto)
                .toList());
    }

    @Override
    @Transactional
    public void deleteFavoriteProductByUserIdAndProductId(UUID userId, UUID productId) {
        FavoriteItem favoriteItem = favoriteItemRepository.getFavoriteItemByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("FavoriteItem was not found by given userId = %s and productId = %s"
                        .formatted(userId, productId))
        );
        favoriteItemRepository.deleteById(favoriteItem.getId());
    }
}