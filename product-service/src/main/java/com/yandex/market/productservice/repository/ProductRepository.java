package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("FROM Product p WHERE p.externalId=:externalId AND p.isDeleted=false")
    Optional<Product> findByExternalId(@Param("externalId") UUID externalId);

    @Query("FROM Product p WHERE p.externalId=:externalId")
    Optional<Product> findNoLimitsByExternalId(@Param("externalId") UUID externalId);

    @Query("FROM Product p WHERE p.externalId in :externals AND p.isDeleted=false")
    Stream<Product> findByExternalId(@Param("externals") Set<UUID> uuidSet, Pageable pageable);
}
