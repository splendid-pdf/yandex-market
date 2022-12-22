package com.yandex.market.shopservice.service.shop;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShopSystemService {
    Page<ShopSystemResponsesDto> getAll(Pageable pageable);

    UUID create(ShopSystemRequestDto dto);

    ShopSystemResponsesDto getDtoByExternalId(UUID externalId);

    ShopSystem getByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

    void updateByExternalId(UUID externalId, ShopSystemRequestDto dto);
}
