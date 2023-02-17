package com.yandex.market.favoritesservice.service;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.mapper.FavoriteItemMapper;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import com.yandex.market.favoritesservice.repository.FavoriteItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoriteItemServiceImpl implements FavoriteItemService {

    private final FavoriteItemMapper mapper;

    private final FavoriteItemRepository repository;

    @Override
    public UUID addItemInFavorites(UUID productId, UUID userId) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setUserId(userId);
        favoriteItem.setProductId(productId);
        favoriteItem.setExternalId(UUID.randomUUID());
        favoriteItem.setAddedAt(LocalDateTime.now());
        return repository.save(favoriteItem).getExternalId();
    }

    @Override
    @Transactional
    public Page<FavoriteItemResponseDto> getFavoritesByUserId(UUID userId, Pageable page) {
        Page<FavoriteItem> pageFavorites = repository.getFavoritesByUserId(userId, page);
        return new PageImpl<>(pageFavorites.getContent().stream()
                .map(mapper::toFavoritesResponseDto)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteFavoritesByUserId(UUID userId, UUID productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }
}