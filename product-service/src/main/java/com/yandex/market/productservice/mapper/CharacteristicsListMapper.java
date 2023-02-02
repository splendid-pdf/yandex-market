package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import com.yandex.market.productservice.model.Characteristic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CharacteristicMapper.class)
public interface CharacteristicsListMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    List<CharacteristicDto> toDtoList(List<Characteristic> characteristicsList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    List<Characteristic> toCharacteristicList(List<CharacteristicDto> characteristicDto);
}
