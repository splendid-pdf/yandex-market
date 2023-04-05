package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.ProductSpecialPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductSpecialPriceRepository extends JpaRepository<ProductSpecialPrice, Long> {

    Optional<ProductSpecialPrice> findByExternalId(UUID externalId);

    void deleteByExternalId(UUID specialPriceExternalId);
}
