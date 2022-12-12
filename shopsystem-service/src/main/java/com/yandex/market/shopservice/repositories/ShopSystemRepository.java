package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShopSystemRepository extends JpaRepository<ShopSystem, Long> {
    // TODO: далее настроить поиск по externalID
    @Query(value = "SELECT ss " +
            "FROM ShopSystem ss " +
            "WHERE ss.externalId=:externalId")
    Optional<ShopSystem> findByExternalId(@Param("externalId") UUID externalId);
}
