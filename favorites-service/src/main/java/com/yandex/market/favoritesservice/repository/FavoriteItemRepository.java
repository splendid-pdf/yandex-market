package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.model.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {

    Optional<FavoriteItem> findFavoriteItemByUserId(UUID userId);
}