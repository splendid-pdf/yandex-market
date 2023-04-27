package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.model.FavoriteItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {

    Page<FavoriteItem> getFavoritesByUserId(UUID userId, Pageable pageable);

    Optional<FavoriteItem> getFavoriteItemByUserId(UUID userId);
}