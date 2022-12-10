package com.market.shopservice.util;

import com.market.shopservice.dto.ShopSystemDto;
import com.market.shopservice.models.shop.ShopSystem;
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
