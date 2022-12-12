package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopSystemUtil {
    private final ModelMapper modelMapper;

    public ShopSystem convertDtoToShopSystem(ShopSystemDto dto) {
        return modelMapper.map(dto, ShopSystem.class);
    }
}
