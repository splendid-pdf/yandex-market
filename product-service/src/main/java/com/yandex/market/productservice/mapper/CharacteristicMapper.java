package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import com.yandex.market.productservice.model.Characteristic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CharacteristicMapper {


    CharacteristicDto toDto(Characteristic charachteristic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Characteristic toCharachteristic(CharacteristicDto characteristicDto);


}
