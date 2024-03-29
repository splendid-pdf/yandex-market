package com.yandex.market.productservice.repository;


import com.yandex.market.productservice.dto.projections.SellerArchiveProductPreview;
import com.yandex.market.productservice.dto.projections.UserProductPreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
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

    @Query("FROM Product p WHERE p.externalId=:productId AND p.isDeleted=false")
    Optional<Product> findByExternalId(@Param("productId") UUID productId);

    @Query(value = """
            SELECT
                p.external_id as id,
                p.name as name,
                p.seller_external_id as sellerId,
                p.brand as brand,
                p.price as price,
                p.count as count,
                types.name as type,
                p.creation_date as creationDate,
                p.is_visible as isVisible,
                product_images.url as imageUrl
            FROM
                products AS p
                LEFT JOIN product_images ON product_images.product_id=p.id
                LEFT JOIN types ON types.id=p.type_id
            WHERE
                p.seller_external_id=:sellerId AND
                p.is_archived=false AND
                product_images.is_main=true AND
                p.is_deleted=false
            """, nativeQuery = true)
    Page<SellerProductPreview> findProductsPreviewBySellerId(@Param("sellerId") UUID sellerId,
                                                             Pageable pageable);

    @Query(value = """
            SELECT
                p.external_id as id,
                p.name as name,
                p.seller_external_id as sellerId,
                p.brand as brand,
                p.price as price,
                p.count as count,
                types.name as type,
                p.creation_date as creationDate,
                product_images.url as imageUrl
            FROM
                products AS p
                LEFT JOIN product_images ON product_images.product_id=p.id
                LEFT JOIN types ON types.id=p.type_id
            WHERE
                p.seller_external_id=:sellerId AND
                p.is_archived=true AND
                product_images.is_main=true
                AND p.is_deleted=false
            """, nativeQuery = true)
    Page<SellerArchiveProductPreview> findArchivedProductsPreviewBySellerId(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isDeleted=true,
                    p.isArchived=false,
                    p.isVisible=false
                WHERE p.isArchived=true AND
                      p.sellerExternalId=:sellerId AND
                      p.externalId IN :productIds
            """)
    void deleteProductsBySellerId(UUID sellerId, List<UUID> productIds);

    @Query(value = """
            SELECT
               p.external_id AS id,
               p.seller_external_id AS sellerId,
               p.name,
               p.price,
               product_image.url AS imageUrl
             FROM products p
             LEFT JOIN product_images product_image ON p.id = product_image.product_id
             WHERE product_image.is_main = true
             ORDER BY p.creation_date DESC
             """,
            nativeQuery = true)
    Page<UserProductPreview> getProductsPreview(Pageable pageable);

    @Query(value = """
            SELECT
               p.external_id AS id,
               p.seller_external_id AS sellerId,
               p.name,
               p.price,
               product_image.url AS imageUrl
             FROM products p
             LEFT JOIN product_images product_image ON p.id = product_image.product_id
             WHERE p.external_id IN ?1 AND product_image.is_main = true
             ORDER BY p.creation_date DESC
             """,
            nativeQuery = true)
    List<UserProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers);

    @Modifying
    @Query("""
            UPDATE Product p
            SET p.price=:updatedPrice
            WHERE p.externalId=:productId
            """)
    void updateProductPrice(UUID productId, Long updatedPrice);

    @Modifying
    @Query("""
            UPDATE Product p
            SET p.count=:updatedCount
            WHERE p.externalId=:productId
            """)
    void updateProductCount(UUID productId, Long updatedCount);

    @Modifying
    @Query(value = """
                UPDATE Product p
                SET p.isArchived=:isArchive,
                    p.isVisible=false
                WHERE p.externalId IN :productIds AND
                      p.isDeleted=false
            """)
    void changeIsArchiveField(List<UUID> productIds, boolean isArchive);

    @Modifying
    @Query(value = """
             UPDATE Product p
             SET p.isVisible=:isVisible
             WHERE p.externalId IN :productIds AND
                   p.isDeleted=false AND
                   p.isArchived=false
         """)
    void changeProductVisibility(List<UUID> productIds, boolean isVisible);
}