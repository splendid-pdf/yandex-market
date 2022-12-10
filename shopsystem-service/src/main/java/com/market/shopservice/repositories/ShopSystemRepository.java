package com.market.shopservice.repositories;

import com.market.shopservice.models.shop.ShopSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopSystemRepository extends JpaRepository<ShopSystem, Long> {
    // TODO: далее
    @Query(value = "", nativeQuery = true)
    ShopSystem findByExternalId(String externalId);
}
