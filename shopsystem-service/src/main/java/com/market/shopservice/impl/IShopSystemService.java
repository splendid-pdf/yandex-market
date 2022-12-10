package com.market.shopservice.impl;

import com.market.shopservice.dto.ShopSystemDto;
import com.market.shopservice.models.shop.ShopSystem;

import java.util.List;
import java.util.UUID;

public interface IShopSystemService {
    List<ShopSystem> getAllShopSystems();

    void createShopSystem(ShopSystemDto dto);

    ShopSystem getShopSystemByExternalId(UUID externalId);

    void deleteSystemShopByExternalId(UUID externalId);
}
