package com.yandex.market.shopservice.impl;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;

import java.util.List;
import java.util.UUID;

public interface IShopSystemService {
    List<ShopSystem> getAllShopSystems();

    void createShopSystem(ShopSystemDto dto);

    ShopSystem getShopSystemByExternalId(UUID externalId);

    void deleteSystemShopByExternalId(UUID externalId);
}
