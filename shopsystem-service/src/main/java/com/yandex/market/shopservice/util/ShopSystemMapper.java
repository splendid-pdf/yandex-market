package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponseDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {BranchMapper.class, SpecialOfferMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface ShopSystemMapper {
    ShopSystemResponseDto toShopSystemResponse(ShopSystem shopSystem);

    ShopSystem toShopSystem(ShopSystemRequestDto request);
}

