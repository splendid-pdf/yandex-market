package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ShopSystemMapper {
    ShopSystemMapper INSTANCE = Mappers.getMapper(ShopSystemMapper.class);
    ShopSystem toShopSystem (ShopSystemDto shopSystemDto);
    List<ShopSystemDto> toPageShopSystemDto(List<ShopSystem> page);
}
