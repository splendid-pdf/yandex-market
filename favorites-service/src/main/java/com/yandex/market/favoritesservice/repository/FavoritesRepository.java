package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.model.Favorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    Page<Favorites> getFavoritesByUserId(UUID userId, Pageable pageable);

    void deleteByUserIdAndProductId(UUID userId, UUID productId);
}
