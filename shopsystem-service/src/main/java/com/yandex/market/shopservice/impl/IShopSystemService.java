package com.yandex.market.shopservice.impl;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IShopSystemService {
    Page<ShopSystemDto> getAllShopSystems(Pageable pageable);

    void createShopSystem(ShopSystemDto dto);

    ShopSystem getShopSystemByExternalId(UUID externalId);

    void deleteSystemShopByExternalId(UUID externalId);
}
