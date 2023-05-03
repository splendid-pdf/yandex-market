package com.yandex.market.favoritesservice.repository;

import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    @Query(value = """
                SELECT
                    f.user_id AS userId,
                    f.external_id AS externalId,
                    f.added_at AS addedAt
                FROM favorite_products f
                WHERE f.user_id = :userId
            """, nativeQuery = true)
    Page<FavoritePreview> findFavoriteProductsByUserId(@Param("userId") UUID userId, Pageable pageable);

    boolean existsFavoriteProductByExternalIdAndUserId(UUID externalId, UUID userId);

    void deleteByExternalIdAndUserId(UUID externalId, UUID userId);
}