package com.yandex.market.productservice.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("FROM Product p WHERE p.externalId=:externalId AND p.isDeleted=false")
    Optional<Product> findByExternalId(@Param("externalId") UUID externalId);
}
