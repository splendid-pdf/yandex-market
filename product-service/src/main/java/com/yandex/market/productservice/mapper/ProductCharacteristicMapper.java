package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductCharacteristicDto;
import com.yandex.market.productservice.model.ProductCharacteristic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCharacteristicMapper {

    ProductCharacteristic toProductCharacteristic(ProductCharacteristicDto productCharacteristicDto);
    ProductCharacteristicDto toProductCharacteristicDto(ProductCharacteristic productCharacteristic);
}
