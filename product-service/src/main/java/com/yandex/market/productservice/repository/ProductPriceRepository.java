package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.dto.response.ProductFullInfoResponse;
import com.yandex.market.productservice.model.ProductPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @Query(value = """
                SELECT new com.yandex.market.productservice.dto.response.ProductFullInfoResponse(
                p.externalId,
                p.name,
                p.description,
                p.productType,
                p.manufacturer,
                p.imageUrl,
                p.weight,
                p.dimensions,
                p.isVisible,
                p.rating,
                pp.branchId,
                pp.price,
                pp.discountedPrice,
                pp.specialPriceFromDate,
                pp.specialPriceToDate)
                FROM ProductPrice pp
                INNER JOIN Product p
                ON p.externalId = pp.productId
                WHERE pp.shopSystemId=:shopId
            """)
    Page<ProductFullInfoResponse> getPageProductsByShopSystemId(@Param("shopId") UUID shopId,
                                                                Pageable pageable);

    @Query(value = """
                SELECT new com.yandex.market.productservice.dto.response.ProductFullInfoResponse(
                p.externalId,
                p.name,
                p.description,
                p.productType,
                p.manufacturer,
                p.imageUrl,
                p.weight,
                p.dimensions,
                p.isVisible,
                p.rating,
                pp.branchId,
                pp.price,
                pp.discountedPrice,
                pp.specialPriceFromDate,
                pp.specialPriceToDate)
                FROM ProductPrice pp
                INNER JOIN Product p
                ON p.externalId = pp.productId
                WHERE pp.branchId=:branchId
            """)
    Page<ProductFullInfoResponse> getPageProductsByBranchId(@Param("branchId") UUID branchId,
                                                                Pageable pageable);

    @Query(value = """
            FROM ProductPrice pp
            WHERE pp.externalId=:externalId
            """)
    Optional<ProductPrice> findByExternalId(UUID externalId);
}
