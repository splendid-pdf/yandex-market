package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CharacteristicMapper.class)
public interface CharacteristicMapper {
    CharacteristicDto toDto(Charachteristic charachteristic);
    Charachteristic toCharachteristic(CharacteristicDto characteristicDto);
}
