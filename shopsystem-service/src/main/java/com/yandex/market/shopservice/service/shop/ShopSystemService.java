package com.yandex.market.shopservice.service.shop;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponseDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShopSystemService {
    Page<ShopSystemResponseDto> getAllShopSystems(Pageable pageable);

    UUID createShopSystem(ShopSystemRequestDto dto);

    ShopSystemResponseDto getShopSystemDtoByExternalId(UUID externalId);

    ShopSystem getShopSystemByExternalId(UUID externalId);

    void deleteShopSystemByExternalId(UUID externalId);

    void updateShopSystemByExternalId(UUID externalId, ShopSystemRequestDto dto);
}
