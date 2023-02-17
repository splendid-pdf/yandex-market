package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.model.FavoriteItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {

    Page<FavoriteItem> getFavoritesByUserId(UUID userId, Pageable pageable);

    void deleteByUserIdAndProductId(UUID userId, UUID productId);
}
