package com.yandex.market.shopservice.service.shop;

import com.yandex.market.shopservice.dto.shop.requests.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.responses.ShopSystemResponsesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShopSystemService {
    Page<ShopSystemResponsesDto> getAllShopSystems(Pageable pageable);

    void createShopSystem(ShopSystemRequestDto dto);

    ShopSystemResponsesDto getShopSystemByExternalId(UUID externalId);

    void deleteSystemShopByExternalId(UUID externalId);

    void updateSystemShopByExternalId(UUID externalId, ShopSystemRequestDto dto);
}
