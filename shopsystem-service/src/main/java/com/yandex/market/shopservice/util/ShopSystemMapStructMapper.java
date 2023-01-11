package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponseDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {BranchMapper.class, SpecialOfferMapper.class}
)
public interface ShopSystemMapStructMapper {
    ShopSystemMapStructMapper INSTANCE = Mappers.getMapper(ShopSystemMapStructMapper.class);

    ShopSystemResponseDto toShopSystemResponse(ShopSystem shopSystem);

    ShopSystem toShopSystem(ShopSystemRequestDto request);
}

