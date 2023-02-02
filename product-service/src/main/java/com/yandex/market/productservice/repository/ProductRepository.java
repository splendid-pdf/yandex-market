package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("FROM Product p WHERE p.externalId=:externalId AND p.isDeleted=false")
    Optional<Product> findByExternalId(@Param("externalId") UUID externalId);

    @Query( "FROM Product p WHERE p.externalId in :externals AND p.isDeleted=false" )
    List<Product> findByExternalId(@Param("externals") Set<UUID> uuidSet, Pageable pageable);
}
