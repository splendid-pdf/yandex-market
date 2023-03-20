package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.dto.response.ProductPreview;
import com.yandex.market.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("FROM Product p WHERE p.externalId=:externalId AND p.isDeleted=false")
    Optional<Product> findByExternalId(@Param("externalId") UUID externalId);

    @Query("FROM Product p WHERE p.externalId in :externals AND p.isDeleted=false")
    Stream<Product> findByExternalId(@Param("externals") Set<UUID> uuidSet, Pageable pageable);

    @Query(value = """
            FROM Product p
            WHERE p.sellerExternalId = :sellerId AND p.isDeleted = false
            """)
    Page<Product> findProductsPageBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Query(value = """
            FROM Product p
            WHERE p.sellerExternalId = :sellerId AND p.isDeleted = true
            """)
    Page<Product> findArchivedProductsPageBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isVisible=false
                WHERE p.sellerExternalId=:sellerId AND p.externalId IN :productIds
            """)
    void hideProductsBySellerId(List<UUID> productIds, UUID sellerId);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isVisible=true
                WHERE p.sellerExternalId=:sellerId AND p.externalId IN :productIds
            """)
    void displayProductsBySellerId(List<UUID> productIds, UUID sellerId);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isVisible=false, p.isDeleted = true
                WHERE p.sellerExternalId=:sellerId AND p.externalId IN :productIds
            """)
    void addProductsToArchiveBySellerId(List<UUID> productIds, UUID sellerId);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isDeleted=false
                WHERE p.sellerExternalId=:sellerId AND p.externalId IN :productIds
            """)
    void returnProductsFromArchiveBySellerId(List<UUID> productIds, UUID sellerId);

    @Modifying
    @Query(value = """
                DELETE FROM Product p
                WHERE p.sellerExternalId=:sellerId AND
                 p.isDeleted=true AND
                 p.externalId IN :productIds
            """)
    void deleteProductsBySellerId(List<UUID> productIds, UUID sellerId);

    @Query(value = """
            SELECT
               p.external_id AS externalId,
               p.seller_external_id AS sellerExternalId,
               p.name,
               p.price,
               product_image.url AS imageUrl
             FROM products p
             LEFT JOIN product_images product_image ON p.id = product_image.product_id
             WHERE product_image.is_main = true
             ORDER BY p.creation_date DESC
             """,
            nativeQuery = true)
    Page<ProductPreview> getProductsPreview(Pageable pageable);

    @Query(value = """
            SELECT
               p.external_id AS externalId,
               p.seller_external_id AS sellerExternalId,
               p.name,
               p.price,
               product_image.url AS imageUrl
             FROM products p
             LEFT JOIN product_images product_image ON p.id = product_image.product_id
             WHERE p.external_id IN ?1 AND product_image.is_main = true
             ORDER BY p.creation_date DESC
             """,
            nativeQuery = true)
    List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers);
}