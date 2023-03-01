package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            FROM Product p
            WHERE p.sellerExternalId = :sellerId AND p.isDeleted = false
            """)
    Page<Product> getPageOfProductsBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Query(value = """
            FROM Product p
            WHERE p.sellerExternalId = :sellerId AND p.isDeleted = true
            """)
    Page<Product> getArchivePageOfProductsBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Modifying
    @Query(value = """
                DELETE FROM Product p
                WHERE p.sellerExternalId=:sellerId AND
                 p.isDeleted=true AND
                 p.externalId IN :productIds
            """)
    void deleteProductsBySellerId(List<UUID> productIds, UUID sellerId);
}