package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import com.yandex.market.productservice.model.Characteristic;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {

    Characteristic toCharacteristic(CharacteristicDto characteristicDto);

    CharacteristicDto toCharacteristicDto(Characteristic characteristic);
}