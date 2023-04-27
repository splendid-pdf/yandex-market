package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    @Query(value = """
                SELECT
                    f.user_id AS userId,
                    fp.external_id AS externalId,
                    fp.added_at AS addedAt
                FROM favorites f
                INNER JOIN favorite_products fp ON f.id = fp.favorite_item_id
                WHERE f.user_id = :userId
            """, nativeQuery = true)
    Page<FavoritePreview> findFavoriteProductsByUserId(@Param("userId") UUID userId, Pageable pageable);

    Optional<FavoriteProduct> findFavoriteItemByFavoriteItemAndExternalId(FavoriteItem favoriteItem, UUID externalId);

    void deleteByFavoriteItemAndExternalId(FavoriteItem favoriteItem, UUID externalId);
}