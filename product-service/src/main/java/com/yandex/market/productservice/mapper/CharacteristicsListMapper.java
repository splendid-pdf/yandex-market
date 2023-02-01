package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import com.yandex.market.productservice.model.Characteristic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CharacteristicMapper.class)
public interface CharacteristicsListMapper {
    List<CharacteristicDto> toDtoList(List<Characteristic> charachteristicsList);
    List<Characteristic> toCharachteristicsList(List<CharacteristicDto> characteristicDto);
}
