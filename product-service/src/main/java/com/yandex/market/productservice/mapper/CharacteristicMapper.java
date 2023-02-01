package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import com.yandex.market.productservice.model.Characteristic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {
    CharacteristicDto toDto(Characteristic charachteristic);
    Characteristic toCharachteristic(CharacteristicDto characteristicDto);
}
