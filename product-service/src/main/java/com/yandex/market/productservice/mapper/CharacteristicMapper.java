package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.TypeCharacteristicDto;
import com.yandex.market.productservice.model.TypeCharacteristic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {

    TypeCharacteristic toCharacteristic(TypeCharacteristicDto typeCharacteristicDto);

    TypeCharacteristicDto toCharacteristicDto(TypeCharacteristic typeCharacteristic);
}